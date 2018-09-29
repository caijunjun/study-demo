package com.study.web.handler.core;

import java.util.List;

import org.springframework.transaction.support.TransactionTemplate;

import com.study.web.handler.HandlerChain;

public class CoreHandlerChain extends HandlerChain<CoreContext, CoreResult, CoreHandler> {

	public CoreHandlerChain(List<CoreHandler> handler, TransactionTemplate transactionTemplate) {
		super(handler, transactionTemplate);
	}

}
