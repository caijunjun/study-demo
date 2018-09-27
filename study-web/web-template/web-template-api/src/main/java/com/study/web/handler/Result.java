package com.study.web.handler;

/**
 * Handler 执行处理结果
 * @author caijunjun
 * @date 2018年9月27日 
 * @param <R>
 */
public interface Result<R> {
	
	/**
	 * 是否结束后续处理
	 * 
	 * @return
	 */
	default boolean isFinish() {
		return true;
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
