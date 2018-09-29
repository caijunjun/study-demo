package com.study.web.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.study.web.handler.HandlerChain;
import com.study.web.handler.core.CoreContext;
import com.study.web.handler.core.CoreHandler;
import com.study.web.handler.core.CoreHandlerChain;
import com.study.web.handler.core.CoreResult;
import com.study.web.handler.self.SelfHandler;
import com.study.web.handler.user.UserContext;
import com.study.web.handler.user.UserHandler;
import com.study.web.handler.user.UserResult;

@Configuration
public class CoreHandlerConfiguration {

	@Autowired
	private TransactionTemplate transactionTemplate;

	/**
	 * 适用场景 业务功能垂直拆分 责任链处理 handler
	 * 
	 * @param selfHandlers
	 * @return
	 */
	@Bean
	public CoreHandlerChain selfHandlerChain(List<CoreHandler> list) {
		return new CoreHandlerChain(list, transactionTemplate);
	}

	@Bean
	public HandlerChain<UserContext, UserResult, UserHandler> userHandlerChain(List<UserHandler> list) {
		return new HandlerChain<>(list, transactionTemplate);
	}

	@Bean
	public HandlerChain<CoreContext, CoreResult, CoreHandler> coreHandler(List<CoreHandler> list) {
		return new HandlerChain<>(list, transactionTemplate);
	}
}
