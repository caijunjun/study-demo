package com.study.web.handler.user;

import org.springframework.stereotype.Service;

import com.study.web.handler.core.CoreContext;
import com.study.web.handler.core.CoreResult;

@Service
public class UserHandler2 implements UserHandler {

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public CoreResult handler(CoreContext coreContext, CoreResult coreResult) {
		return null;
	}


}
