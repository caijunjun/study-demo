package com.study.web.controller.self;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.util.IdUtil;
import com.study.web.entity.UserInfo;
import com.study.web.handler.user.UserContext;
import com.study.web.handler.user.UserHandlerChain;
import com.study.web.service.user.UserInfoService;

@RestController
public class SelfController {

	/**
	 * 垂直扩展
	 */
	@Autowired
	private UserHandlerChain userHandlerChain;
	
	@Autowired
	private UserInfoService userInfoService;
	
	
	@RequestMapping(value = "/self/cloud/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserInfo createUserInfo(@RequestBody UserInfo userInfo) {
		
//		userInfoService.addUserInfo(userInfo);
		
		UserContext userContext=new UserContext();
		userInfo.setId(IdUtil.getLongId());
		userInfo.setName("测试名称");
		userInfo.setCreateTime(new Date());
		userContext.setUserInfo(userInfo);
		
		userHandlerChain.handler(userContext,false);
		
		return userInfo;
	}
}
