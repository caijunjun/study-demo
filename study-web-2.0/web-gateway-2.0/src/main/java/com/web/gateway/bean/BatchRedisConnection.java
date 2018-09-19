package com.web.gateway.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

public class BatchRedisConnection implements CoreOperations, InitializingBean {

	private RedisConnection connection;

	private RedisSerializer<?> keySerializer = null;
	private RedisSerializer<?> valueSerializer = null;
	private RedisSerializer<?> hashKeySerializer = null;
	private RedisSerializer<?> hashValueSerializer = null;
	private RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

	public BatchRedisConnection(RedisConnection connection) {
		this.connection = connection;
	}

	public RedisConnection getInnerConnection() {
		return connection;
	}

	@Override
	public <K, V> void set(K key, V value) {
		connection.set(serializerKey(key), serializeValue(value));
	}

	@Override
	public <K, V> void set(K key, final V value, final long offset) {
		connection.setRange(serializerKey(key), serializeValue(value), offset);
	}

	@Override
	public <K, V> V getAndSet(K key, V newValue) {
		return deserializeValue(connection.getSet(serializerKey(key), serializeValue(newValue)));
	}

	@Override
	public <K> long increment(K key, final long delta) {
		return connection.incrBy(serializerKey(key), delta);
	}

	@Override
	public <K> long increment(K key, final long delta, final long timeout, final TimeUnit unit) {
		byte[] bytes = serializerKey(key);
		Long incrDelta = connection.incrBy(bytes, delta);
		expire(bytes, timeout, unit);
		return incrDelta;
	}

	@Override
	public <K> Boolean expire(K key, final long timeout, final TimeUnit unit) {
		if (timeout <= 0) {
			return true;
		}
		if (TimeUnit.MILLISECONDS == unit) {
			return connection.pExpire(serializerKey(key), unit.toMillis(timeout));
		} else {
			return connection.expire(serializerKey(key), unit.toSeconds(timeout));
		}
	}

	@Override
	public <K, V> void set(K key, V value, final long timeout, final TimeUnit unit) {
		if (timeout <= 0) {
			set(key, value);
			return;
		}
		final byte[] rawKey = serializerKey(key);
		final byte[] rawValue = serializeValue(value);

		if (TimeUnit.MILLISECONDS == unit) {
			connection.pSetEx(rawKey, timeout, rawValue);
		} else {
			connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
		}
	}

	@Override
	public <K> Boolean setBit(K key, final long offset, final boolean value) {
		return connection.setBit(serializerKey(key), offset, value);
	}

	@Override
	public <K> Boolean getBit(K key, final long offset) {
		return connection.getBit(serializerKey(key), offset);
	}

	@Override
	public <K> Long size(K key) {
		return connection.strLen(serializerKey(key));
	}

	@Override
	public <K, HK, HV> void hSet(K key, HK hashKey, HV haseValue) {
		connection.hSet(serializerKey(key), serializeHashKey(hashKey), serializeHashValue(haseValue));
	}

	@Override
	public <K> String get(K key, final long start, final long end) {
		return deserializeStringValue(connection.getRange(serializerKey(key), start, end));
	}

	@Override
	public <V, K> V get(K key) {
		return deserializeValue(connection.get(serializerKey(key)));
	}

	@Override
	public <K> boolean exists(K key) {
		Boolean flag = connection.exists(serializerKey(key));
		return flag != null && flag;
	}
	
	@Override
	public <V, K> V cache(K key, V value, long timeout, TimeUnit unit) {
		// 如果保存成功，说明原来不存在key
		if (setNX(key, value, timeout, unit)) {
			return value;
		} else {
			// 延续过期时间
			expire(key, timeout, unit);
			return get(key);
		}
	}

	@Override
	public <V, K> V cache(K key, V value) {
		if (setNX(key, value)) {
			return value;
		} else {
			return get(key);
		}
	}

	@Override
	public <K> Integer getIntValue(K key) {
		Integer value = deserializeValue(connection.get(serializerKey(key)));
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Override
	public <HV, K, HK> HV hGet(K key, HK hashKey) {
		return deserializeHashValue(connection.hGet(serializerKey(key), serializeHashKey(hashKey)));
	}

	@Override
	public <K, V> Set<V> hkeys(K key) {
		return deserializeSet(connection.hKeys(serializerKey(key)));
	}

	@Override
	public <K> long hLen(K key) {
		Long len = connection.hLen(serializerKey(key));
		if (len == null) {
			return 0;
		} else {
			return len;
		}
	}

	@Override
	public <K> boolean delete(K key) {
		return connection.del(serializerKey(key)) > 0;
	}

	@Override
	public <K, HK> Long hDel(K key, HK hashKey) {
		return connection.hDel(serializerKey(key), serializeHashKey(hashKey));
	}

	@Override
	public Cursor<byte[]> scan(String pattern, long count) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
		return connection.scan(scanOptions);
	}

	@Override
	public <K> Cursor<Map.Entry<byte[], byte[]>> hScan(K key, String pattern, long count) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
		return connection.hScan(serializerKey(key), scanOptions);
	}

	@Override
	public <K, HK, HV> Map<HK, HV> hScanEntries(K key, String pattern, long count) {
		Map<HK, HV> map = new LinkedHashMap<>();
		Cursor<Entry<byte[], byte[]>> cursor = hScan(key, pattern, count);
		while (cursor.hasNext()) {
			map.put(deserializeHashKey(cursor.next().getKey()), deserializeHashValue(cursor.next().getValue()));
		}

		return map;
	}

	@Override
	public <K, HK> Set<HK> hScanKeys(K key, String pattern, long count) {
		Set<HK> set = new LinkedHashSet<>();
		Cursor<Entry<byte[], byte[]>> cursor = hScan(key, pattern, count);
		while (cursor.hasNext()) {
			set.add(deserializeHashKey(cursor.next().getKey()));
		}
		return set;
	}

	@Override
	public <K, HV> Collection<HV> hScanValues(Collection<HV> collection, K key, String pattern, long count) {
		Cursor<Entry<byte[], byte[]>> cursor = hScan(key, pattern, count);
		while (cursor.hasNext()) {
			collection.add(deserializeHashValue(cursor.next().getValue()));
		}
		return collection;
	}

	@Override
	public boolean clean(String pattern, long count) {
		Cursor<byte[]> cursor = scan(pattern, count);
		while (cursor.hasNext()) {
			connection.del(cursor.next());
		}
		return true;
	}

	@Override
	public <K> Map<byte[], byte[]> entriesByte(K key) {
		return connection.hGetAll(serializerKey(key));
	}

	@Override
	public <K, HK, HV> Map<HK, HV> entries(K key) {
		return deserializeHashMap(entriesByte(key));
	}

	@Override
	public <K> Long ttl(K key) {
		return connection.ttl(serializerKey(key));
	}

	@Override
	public <K, V> Boolean setNX(K key, V value) {
		return connection.setNX(serializerKey(key), serializeValue(value));
	}

	@Override
	public <K, V> Boolean setNX(K key, V value, long timeout, TimeUnit unit) {
		if (setNX(key, value)) {
			expire(key, timeout, unit);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public <K, V> Boolean sIsMember(K key, V value) {
		return connection.sIsMember(serializerKey(key), serializeValue(value));
	}

	@Override
	public <K, V> Long sAdd(K key, V[] values) {
		byte[][] byteValues = new byte[values.length][];
		for (int i = 0; i < values.length; i++) {
			byteValues[i] = serializeValue(values[i]);
		}

		return connection.sAdd(serializerKey(key), byteValues);
	}

	// =================分隔符=======================

	@SuppressWarnings("unchecked")
	public <K> RedisSerializer<K> getKeySerializer() {
		return (RedisSerializer<K>) keySerializer;
	}

	public void setKeySerializer(RedisSerializer<?> keySerializer) {
		this.keySerializer = keySerializer;
	}

	@SuppressWarnings("unchecked")
	public <V> RedisSerializer<V> getValueSerializer() {
		return (RedisSerializer<V>) valueSerializer;
	}

	public void setValueSerializer(RedisSerializer<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	@SuppressWarnings("unchecked")
	public <HK> RedisSerializer<HK> getHashKeySerializer() {
		return (RedisSerializer<HK>) hashKeySerializer;
	}

	public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
		this.hashKeySerializer = hashKeySerializer;
	}

	@SuppressWarnings("unchecked")
	public <HV> RedisSerializer<HV> getHashValueSerializer() {
		return (RedisSerializer<HV>) hashValueSerializer;
	}

	public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
		this.hashValueSerializer = hashValueSerializer;
	}

	private <K> byte[] serializerKey(K key) {
		return serializer(key, getKeySerializer());
	}

	@SuppressWarnings({ "unused" })
	private <K> K deserializerKey(byte[] bytes) {
		return deserialize(bytes, getKeySerializer());
	}

	private <V> byte[] serializeValue(V value) {
		return serializer(value, getValueSerializer());
	}

	private <V> V deserializeValue(byte[] bytes) {
		return deserialize(bytes, getValueSerializer());
	}

	private String deserializeStringValue(byte[] bytes) {
		return deserialize(bytes, stringRedisSerializer);
	}

	private <HK> byte[] serializeHashKey(HK hashKey) {
		return serializer(hashKey, getHashKeySerializer());
	}

	private <HK> HK deserializeHashKey(byte[] bytes) {
		return deserialize(bytes, getHashKeySerializer());
	}

	private <HV> byte[] serializeHashValue(HV hashValue) {
		return serializer(hashValue, getHashValueSerializer());
	}

	private <HV> HV deserializeHashValue(byte[] bytes) {
		return deserialize(bytes, getHashValueSerializer());
	}

	private <T> byte[] serializer(T t, RedisSerializer<T> redisSerializer) {
		if (t instanceof byte[]) {
			return (byte[]) t;
		}
		return redisSerializer.serialize(t);
	}

	private <T> T deserialize(byte[] bytes, RedisSerializer<T> redisSerializer) {
		return redisSerializer.deserialize(bytes);
	}

	private <HK> Set<HK> deserializeSet(Set<byte[]> byteSet) {
		if (byteSet == null) {
			return null;
		}
		Set<HK> set = new HashSet<>(byteSet.size());

		byteSet.forEach(bytes -> {
			set.add(deserializeHashKey(bytes));

		});

		return set;
	}

	private <HK, HV> Map<HK, HV> deserializeHashMap(Map<byte[], byte[]> entries) {
		if (entries == null) {
			return null;
		}
		Map<HK, HV> map = new LinkedHashMap<>(entries.size());

		for (Map.Entry<byte[], byte[]> entry : entries.entrySet()) {
			map.put(deserializeHashKey(entry.getKey()), deserializeHashValue(entry.getValue()));
		}
		return map;
	}

	@Override
	public boolean isRepeatClick(String key, long seconds) {
		return !setNX(RedisPrefix.LOCK_REPEAT + key, true, seconds, TimeUnit.SECONDS);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(keySerializer, "keySerializer is null");
		Assert.notNull(valueSerializer, "valueSerializer is null");
		Assert.notNull(hashKeySerializer, "hashKeySerializer is null");
		Assert.notNull(hashValueSerializer, "hashValueSerializer is null");
	}

}