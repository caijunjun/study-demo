package com.study.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.study.web.handler.core.CoreHandlerChain;
import com.study.web.handler.self.SelfContext;
import com.study.web.handler.self.SelfHandler;
import com.study.web.handler.user.UserHandler;
import com.study.web.handler.userself.UserSelfHandler;

@Configuration
public class CoreHandlerConfiguration {

	/**
	 * 适用场景 业务功能垂直拆分 责任链处理 handler
	 * 
	 * @param selfHandlers
	 * @return
	 */
	@Bean
	public CoreHandlerChain selfHandlerChain(SelfHandler[] selfHandlers) {
		return new CoreHandlerChain(selfHandlers);
	}

	@Bean
	public CoreHandlerChain userHandlerChain(UserHandler[] userHandlers) {
		return new CoreHandlerChain(userHandlers);
	}

	@Bean
	public CoreHandlerChain userSelfHandlerExtend(UserSelfHandler[] userSelfHandler ) {
		CoreHandlerChain coreHandlerChain= new CoreHandlerChain(userSelfHandler);
		coreHandlerChain.handler(new SelfContext());
		return coreHandlerChain;
	}

}
