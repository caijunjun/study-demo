package com.study.web.handler;

/**
 * Handler结果
 * 
 * @author caijunjun
 * @date 2018年9月29日
 * @param <R>
 */
public interface Result<R> {

	/**
	 * 是否结束后续handler
	 * 
	 * @return
	 */
	default boolean isFinish() {
		return false;
	}

	/**
	 * 获得上一个handler处理结果
	 * 
	 * @return
	 */
	default R getPrevResult() {
		return null;
	}
}
