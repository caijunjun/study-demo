package com.study.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.web.cloud.UserInfoCloudService;
import com.study.web.entity.UserInfo;
import com.study.web.service.user.UserInfoService;

@RestController
public class UserInfoController implements UserInfoCloudService {

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public UserInfo getUserInfo(@RequestParam("uid") long uid) {
		return userInfoService.getUserInfo(uid);
	}

	@Override
	public UserInfo addUserInfo(@RequestBody UserInfo userInfo) {
		return userInfoService.addUserInfo(userInfo);
	}

}
