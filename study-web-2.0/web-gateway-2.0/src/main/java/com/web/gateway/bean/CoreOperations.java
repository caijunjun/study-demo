package com.web.gateway.bean;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.Cursor;

/**
 * 
 * @Title: CoreOperations.java
 * @Package com.sharetimes.core.config.redis.extend
 * @Description: 核心操作方法
 * @author caiJunJun
 * @date 2017年10月19日
 *
 */
public interface CoreOperations {

	/**
	 * 设置redis值
	 * 
	 * @param key
	 * @param value
	 */
	<K, V> void set(K key, V value);

	/**
	 * 获取redis值
	 * 
	 * @param key
	 * @return
	 */
	<V, K> V get(K key);

	/**
	 * 是否存在指定的key
	 * 
	 * @param key
	 * @return
	 */
	<K> boolean exists(K key);

	/**
	 * 缓存某个值，如果存在，那么直接返回，如果不存在，那么创建
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 * @return
	 */
	<V, K> V cache(K key, V value, long timeout, TimeUnit unit);

	<V, K> V cache(K key, V value);

	<K> Integer getIntValue(K key);

	/**
	 * 这个命令的作用是覆盖key对应的string的一部分，从指定的offset处开始，覆盖value的长度。如果offset比当前key对应string还要长，那这个string后面就补0以达到offset
	 * 
	 * @param key
	 * @param value
	 * @param offset
	 */
	<K, V> void set(K key, final V value, final long offset);

	/**
	 * 返回旧值，不存在返回null，并用新值覆盖
	 * 
	 * @param key
	 * @param newValue
	 */
	<K, V> V getAndSet(K key, V newValue);

	/**
	 * 递增 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 +1
	 * 
	 * @param key
	 * @param delta
	 * @return
	 */
	<K> long increment(K key, final long delta);

	/**
	 * 递增 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 +1 并设置该key有效期
	 * 
	 * @param key
	 * @param delta
	 * @param timeout
	 * @param unit
	 * @return
	 */
	<K> long increment(K key, long delta, long timeout, TimeUnit unit);

	/**
	 * 设置key有效期
	 * 
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	<K> Boolean expire(K key, long timeout, TimeUnit unit);

	/**
	 * 设置redis值（有有效期）
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 */
	<K, V> void set(K key, V value, final long timeout, final TimeUnit unit);

	/**
	 * 返回值的长度
	 * 
	 * @param key
	 * @return
	 */
	<K> Long size(K key);

	/**
	 * 返回指定位置，指定长度的value
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	<K> String get(K key, final long start, final long end);

	/**
	 * hash 设置值
	 * 
	 * @param key
	 * @param hashKey
	 * @param haseValue
	 */
	<K, HK, HV> void hSet(K key, HK hashKey, HV haseValue);

	/**
	 * 获得 hash 值
	 * 
	 * @param key
	 * @param hashKey
	 * @return
	 */
	<HV, K, HK> HV hGet(K key, HK hashKey);

	/**
	 * 获取hash keys
	 * 
	 * @param key
	 * @return
	 */
	<K, V> Set<V> hkeys(K key);

	/**
	 * 获取hash 的长度
	 * 
	 * @param key
	 * @return
	 */
	<K> long hLen(K key);

	/**
	 * 设置boolean类型值
	 * 
	 * @param key
	 * @param offset
	 * @param value
	 * @return
	 */
	<K> Boolean setBit(K key, long offset, boolean value);

	/**
	 * 获得boolean类型值
	 * 
	 * @param key
	 * @param offset
	 * @return
	 */
	<K> Boolean getBit(K key, long offset);

	/**
	 * 删除操作
	 * 
	 * @param key
	 * @return
	 */
	<K> boolean delete(K key);

	/**
	 * hash删除
	 * 
	 * @param key
	 * @param hashKey
	 * @return
	 */
	<K, HK> Long hDel(K key, HK hashKey);

	/**
	 * 获取Cursor对象，用完记得close
	 * 
	 * @param pattern
	 *            需要模糊查询的key，支持*号匹配
	 * @param count
	 *            返回条数
	 * @return
	 */
	Cursor<byte[]> scan(String pattern, long count);

	/**
	 * 获取hash Cursor对象，用完记得close
	 * 
	 * @param key
	 * @param pattern
	 * @param count
	 * @return
	 */
	<K> Cursor<Map.Entry<byte[], byte[]>> hScan(K key, String pattern, long count);

	/**
	 * 用hScan 获取hash 某个 key下 所有的Entry(HK,HV)
	 * 
	 * @param key
	 * @param pattern
	 * @param count
	 * @return
	 */
	<K, HK, HV> Map<HK, HV> hScanEntries(K key, String pattern, long count);

	/**
	 * 用hScan 获取hash 某个 key下 所有的hkey(HK)
	 * 
	 * @param key
	 * @param pattern
	 * @param count
	 * @return
	 */
	<K, HK> Set<HK> hScanKeys(K key, String pattern, long count);

	/**
	 * 用hScan 获取hash 某个 key下 所有的value(HV)
	 * 
	 * @param key
	 * @param pattern
	 * @param count
	 * @return
	 */
	<K, HV> Collection<HV> hScanValues(Collection<HV> collection, K key, String pattern, long count);

	/**
	 * 清除缓存
	 * 
	 * @param pattern
	 *            需要模糊查询的key，支持*号匹配
	 * @param count
	 *            返回条数0为删除所有匹配的
	 * @return
	 * @throws IOException
	 */
	boolean clean(String pattern, long count) throws IOException;

	/**
	 * 返回原生map
	 * 
	 * @param key
	 * @return
	 */
	<K> Map<byte[], byte[]> entriesByte(K key);

	/**
	 * 返回对应类型的map
	 * 
	 * @param key
	 * @return
	 */
	<K, HK, HV> Map<HK, HV> entries(K key);

	/**
	 * 获得key 剩余时间
	 * 
	 * @param key
	 * @return
	 */
	<K> Long ttl(K key);

	/**
	 * 设置key,如果key存在，返回false，如果不存在，返回true(可以用于redis锁)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	<K, V> Boolean setNX(K key, V value);

	<K, V> Boolean setNX(K key, V value, final long timeout, final TimeUnit unit);

	/**
	 * 该key是不是重复点击，如果是，那么返回false，如果不是，那么设置seconds 秒时间内不能重复点击
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	boolean isRepeatClick(String key, long seconds);

	/**
	 * set key集合是否存在value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	<K, V> Boolean sIsMember(K key, V value);

	/**
	 * 创建set集合
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	<K, V> Long sAdd(K key, V[] values);
}
