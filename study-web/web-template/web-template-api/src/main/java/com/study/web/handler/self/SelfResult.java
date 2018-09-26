package com.study.web.handler.self;

import com.study.web.handler.CoreResult;

public class SelfResult implements CoreResult {
	
	private boolean isFinish;

	private SelfResult result;

	public SelfResult(boolean isFinish, SelfResult result) {
		super();
		this.isFinish = isFinish;
		this.result = result;
	}

	@Override
	public boolean isFinish() {
		return isFinish;
	}

	@Override
	public SelfResult getPrevResult() {
		return result;
	}

}
