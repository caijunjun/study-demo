package com.web.gateway.bean;

public class RedisPrefix {

	private RedisPrefix() {
	}

	/**
	 * 通用前缀
	 */
	public static final String COMMON_REDIS_KEY_PREFIX = "COMMON:CACHE:";
	/**
	 * 重复点击锁
	 */
	public static final String LOCK_REPEAT = "LOCK_REPEAT:";

}
