package com.study.web.handler;

import org.springframework.core.Ordered;

public interface CoreHandler extends Ordered {

	/**
	 * 处理方法主入口
	 * 
	 * @param coreContext
	 *            需要处理的上下文
	 * @param coreResult
	 *            上一次handler处理结果
	 * @return
	 */
	CoreResult handler(CoreContext coreContext, CoreResult coreResult);

}
