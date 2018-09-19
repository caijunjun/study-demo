package com.web.gateway.bean;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class BatchRedisTemplate implements CoreOperations, InitializingBean {

	private RedisTemplate<?, ?> redisTemplate;

	public BatchRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public <T> T execute(BatchRedisCallback<T> batchRedisCallback) {
		RedisCallback<T> redisCallback = connection -> {
			BatchRedisConnection batchConnection = new BatchRedisConnection(connection);
			batchConnection.setKeySerializer(redisTemplate.getKeySerializer());
			batchConnection.setValueSerializer(redisTemplate.getValueSerializer());
			batchConnection.setHashKeySerializer(redisTemplate.getHashKeySerializer());
			batchConnection.setHashValueSerializer(redisTemplate.getHashValueSerializer());
			return batchRedisCallback.doInRedis(connection, batchConnection);
		};
		return redisTemplate.execute(redisCallback);
	}

	public RedisTemplate<?, ?> getRedisTemplate() {
		return redisTemplate;
	}

	@Override
	public <K, V> void set(K key, V value) {
		execute((connection, batchConnection) -> {
			batchConnection.set(key, value);
			return null;
		});
	}

	@Override
	public <V, K> V get(K key) {
		return execute((connection, batchConnection) -> batchConnection.get(key));
	}

	@Override
	public <K> boolean exists(K key) {
		return execute((connection, batchConnection) -> batchConnection.exists(key));
	}

	@Override
	public <V, K> V cache(K key, V value, long timeout, TimeUnit unit) {
		return execute((connection, batchConnection) -> batchConnection.cache(key, value, timeout, unit));
	}

	@Override
	public <V, K> V cache(K key, V value) {
		return execute((connection, batchConnection) -> batchConnection.cache(key, value));
	}

	@Override
	public <K> Integer getIntValue(K key) {
		Integer value = get(key);
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Override
	public <K, V> void set(K key, V value, long offset) {
		execute((connection, batchConnection) -> {
			batchConnection.set(key, value, offset);
			return null;
		});

	}

	@Override
	public <K, V> V getAndSet(K key, V newValue) {
		return execute((connection, batchConnection) -> {
			return batchConnection.getAndSet(key, newValue);
		});

	}

	@Override
	public <K> long increment(K key, long delta) {
		return execute((connection, batchConnection) -> batchConnection.increment(key, delta));
	}

	@Override
	public <K> long increment(K key, long delta, long timeout, TimeUnit unit) {
		return execute((connection, batchConnection) -> batchConnection.increment(key, delta, timeout, unit));
	}

	@Override
	public <K> Boolean expire(K key, long timeout, TimeUnit unit) {
		return execute((connection, batchConnection) -> batchConnection.expire(key, timeout, unit));
	}

	@Override
	public <K, V> void set(K key, V value, long timeout, TimeUnit unit) {
		execute((connection, batchConnection) -> {
			batchConnection.set(key, value, timeout, unit);
			return null;
		});
	}

	@Override
	public <K> Long size(K key) {
		return execute((connection, batchConnection) -> batchConnection.size(key));
	}

	@Override
	public <K> String get(K key, long start, long end) {
		return execute((connection, batchConnection) -> batchConnection.get(key, start, end));
	}

	@Override
	public <K, HK, HV> void hSet(K key, HK hashKey, HV haseValue) {
		execute((connection, batchConnection) -> {
			batchConnection.hSet(key, hashKey, haseValue);
			return null;
		});

	}

	@Override
	public <HV, K, HK> HV hGet(K key, HK hashKey) {
		return execute((connection, batchConnection) -> batchConnection.hGet(key, hashKey));
	}

	@Override
	public <K, V> Set<V> hkeys(K key) {
		return execute((connection, batchConnection) -> batchConnection.hkeys(key));
	}

	@Override
	public <K> long hLen(K key) {
		return execute((connection, batchConnection) -> batchConnection.hLen(key));
	}

	@Override
	public <K> Boolean setBit(K key, long offset, boolean value) {
		return execute((connection, batchConnection) -> batchConnection.setBit(key, offset, value));
	}

	@Override
	public <K> Boolean getBit(K key, long offset) {
		return execute((connection, batchConnection) -> batchConnection.getBit(key, offset));
	}

	@Override
	public <K> boolean delete(K key) {
		return execute((connection, batchConnection) -> batchConnection.delete(key));
	}

	@Override
	public <K, HK> Long hDel(K key, HK hashKey) {
		return execute((connection, batchConnection) -> batchConnection.hDel(key, hashKey));
	}

	@Override
	public Cursor<byte[]> scan(String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.scan(pattern, count));
	}

	@Override
	public <K> Cursor<Entry<byte[], byte[]>> hScan(K key, String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.hScan(key, pattern, count));
	}

	@Override
	public <K, HK, HV> Map<HK, HV> hScanEntries(K key, String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.hScanEntries(key, pattern, count));
	}

	@Override
	public <K, HK> Set<HK> hScanKeys(K key, String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.hScanKeys(key, pattern, count));
	}

	@Override
	public <K, HV> Collection<HV> hScanValues(Collection<HV> collection, K key, String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.hScanValues(collection, key, pattern, count));
	}

	@Override
	public boolean clean(String pattern, long count) {
		return execute((connection, batchConnection) -> batchConnection.clean(pattern, count));
	}

	@Override
	public <K> Map<byte[], byte[]> entriesByte(K key) {
		return execute((connection, batchConnection) -> batchConnection.entriesByte(key));
	}

	@Override
	public <K, HK, HV> Map<HK, HV> entries(K key) {
		return execute((connection, batchConnection) -> batchConnection.entries(key));
	}

	@Override
	public <K> Long ttl(K key) {
		return execute((connection, batchConnection) -> batchConnection.ttl(key));
	}

	@Override
	public <K, V> Boolean setNX(K key, V value) {
		return execute((connection, batchConnection) -> batchConnection.setNX(key, value));
	}

	@Override
	public <K, V> Boolean setNX(K key, V value, long timeout, TimeUnit unit) {
		return execute((connection, batchConnection) -> batchConnection.setNX(key, value, timeout, unit));
	}

	@Override
	public boolean isRepeatClick(String key, long seconds) {
		return execute((connection, batchConnection) -> batchConnection.isRepeatClick(key, seconds));
	}

	@Override
	public <K, V> Boolean sIsMember(K key, V value) {
		return execute((connection, batchConnection) -> batchConnection.sIsMember(key, value));
	}

	@Override
	public <K, V> Long sAdd(K key, V[] values) {
		return execute((connection, batchConnection) -> batchConnection.sAdd(key, values));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redisTemplate, "redisTemplate is null");
	}

	public interface BatchRedisCallback<T> {

		/**
		 * 操作
		 * 
		 * @param connection
		 *            RedisConnection原生
		 * @param batchConnection
		 *            扩展接口
		 * @return
		 */
		T doInRedis(RedisConnection connection, BatchRedisConnection batchConnection);

	}
}