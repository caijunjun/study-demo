package com.study.web.handler;

import org.springframework.core.Ordered;

/**
 * 泛型Handler
 * 
 * @author caijunjun
 * @date 2018年9月29日
 * @param <T>
 * @param <R>
 */
public interface Handler<T extends Context, R extends Result<R>> extends Ordered {

	/**
	 * 处理方法主入口
	 * 
	 * @param context
	 *            处理上下文
	 * @param result
	 *            上一次handler处理结果
	 * @return
	 */
	R handler(T context, R result);
	
	@Override
	default int getOrder() {
		return getHandlerOrder();
	}

	/**
	 * handler 执行顺序
	 * @return
	 */
	int getHandlerOrder();
}
