package com.study.web.handler.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.study.web.handler.HandlerChainProxy;

@Component
public class UserHandlerProxy extends HandlerChainProxy<UserHandler> {

	public UserHandlerProxy(List<UserHandler> handlerList, TransactionTemplate transactionTemplate) {
		super(handlerList, transactionTemplate);
	}



}
