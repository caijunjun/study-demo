package com.study.web.handler.self;

import org.springframework.stereotype.Service;

import com.study.web.handler.CoreContext;
import com.study.web.handler.CoreResult;
import com.study.web.handler.userself.UserSelfHandler;

/**
 * 第一个执行器
 * 
 * @author caijunjun
 * @date 2018年9月26日
 */
@Service
public class SelfHandler11 extends SelfHandler1 implements UserSelfHandler {

	@Override
	public int getOrder() {
		return super.getOrder() + 1;
	}

	@Override
	public CoreResult handler(CoreContext coreContext, CoreResult coreResult) {
		System.out.println("start 11");
		System.out.println("start TODO 11");
		System.out.println("end 11");
		return null;
	}

}
