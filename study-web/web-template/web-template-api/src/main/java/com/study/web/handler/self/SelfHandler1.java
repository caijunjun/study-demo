package com.study.web.handler.self;

import org.springframework.stereotype.Service;

import com.study.web.handler.CoreContext;
import com.study.web.handler.CoreResult;

/**
 * 第一个执行器
 * 
 * @author caijunjun
 * @date 2018年9月26日
 */
@Service
public class SelfHandler1 implements SelfHandler {


	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public CoreResult handler(CoreContext coreContext, CoreResult coreResult) {
		System.out.println("start 1");
		System.out.println("start TODO");
		System.out.println("end 1");
		return null;
	}


}
