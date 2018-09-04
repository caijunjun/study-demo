package com.study.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.study.web.entity.UserInfo;
import com.study.web.feign.UserInfoHystrixService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@Autowired
	private UserInfoHystrixService userInfoHystrixService;
	
	@Test
	public void test() {
		userInfoHystrixService.getUserInfo(1);
		UserInfo userInfo=new UserInfo();
		userInfo.setName("userName");
		userInfoHystrixService.addUserInfo(userInfo);
		System.out.println(1);
//		eureka-service-template
	}
}
