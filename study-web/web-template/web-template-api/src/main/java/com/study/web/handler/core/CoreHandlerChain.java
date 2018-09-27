package com.study.web.handler.core;

public class CoreHandlerChain {

	private CoreHandler[] coreHandler;

	public CoreHandlerChain(CoreHandler[] coreHandlers) {
		super();
		this.coreHandler = coreHandlers;
	}

	public CoreResult handler(CoreContext coreContext) {
		CoreResult coreResult = null;
		for (int i = 0; i < coreHandler.length; i++) {
			coreResult = coreHandler[i].handler(coreContext, coreResult);
			if (coreResult!=null&&coreResult.isFinish()) {
				break;
			}
		}
		return coreResult;
	}
}
