package com.study.web.handler;

public interface CoreResult {

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
	default CoreResult getPrevResult() {
		return null;
	}
}
