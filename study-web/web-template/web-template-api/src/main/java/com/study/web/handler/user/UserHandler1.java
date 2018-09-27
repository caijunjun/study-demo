package com.study.web.handler.user;

import org.springframework.stereotype.Service;

import com.study.web.handler.core.CoreContext;
import com.study.web.handler.core.CoreResult;
import com.study.web.handler.userself.UserSelfHandler;

@Service
public class UserHandler1 implements UserHandler,UserSelfHandler {

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CoreResult handler(CoreContext coreContext, CoreResult coreResult) {
		System.out.println("UserHandler1");
		return null;
	}

}
