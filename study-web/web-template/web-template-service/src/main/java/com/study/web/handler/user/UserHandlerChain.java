package com.study.web.handler.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.study.web.handler.HandlerChain;

@Component
public class UserHandlerChain extends HandlerChain<UserContext, UserResult, UserHandler> {

	public UserHandlerChain(List<UserHandler> handlerList, TransactionTemplate transactionTemplate) {
		super(handlerList, transactionTemplate);
	}

}
