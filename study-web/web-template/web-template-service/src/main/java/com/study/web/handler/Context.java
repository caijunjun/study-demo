package com.study.web.handler;

/**
 * 操作对象Context
 * 
 * @author caijunjun
 * @date 2018年9月29日
 */
public interface Context {

	/**
	 * 是否开启事务
	 * 
	 * @return
	 */
	default boolean isOpenTransaction() {
		return false;
	}
}
