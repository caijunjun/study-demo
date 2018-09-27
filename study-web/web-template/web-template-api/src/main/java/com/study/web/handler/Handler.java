package com.study.web.handler;

/**
 * 泛型Handler
 * @author caijunjun
 * @date 2018年9月27日 
 * @param <T>
 * @param <R>
 */
public interface Handler<T extends Context, R extends Result<R>> {

	/**
	 * 处理方法主入口
	 * @param context 处理上下文
	 * @param result 上一次handler处理结果
	 * @return
	 */
	R handler(T context, R result);
}
