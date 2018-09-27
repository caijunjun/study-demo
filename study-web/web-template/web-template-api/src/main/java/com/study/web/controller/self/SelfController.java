package com.study.web.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.web.entity.UserInfo;
import com.study.web.handler.core.CoreHandlerChain;
import com.study.web.handler.self.SelfContext;

@Controller
public class SelfController {

	/**
	 * 垂直扩展
	 */
	@Qualifier("userSelfHandlerExtend")
	@Autowired
	private CoreHandlerChain coreHandlerChain;
	
	
	
	@RequestMapping(value = "/self/cloud/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserInfo createUserInfo(@RequestBody UserInfo userInfo) {
		coreHandlerChain.handler(new SelfContext());
		return null;
	}
}
