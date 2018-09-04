package com.study.web.cloud;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.web.entity.UserInfo;

public interface UserInfoCloudService {

	@RequestMapping(value = "/userInfo/cloud/get", method = RequestMethod.GET)
	UserInfo getUserInfo(@RequestParam("uid") long uid);

	@RequestMapping(value = "/userInfo/cloud/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	UserInfo addUserInfo(@RequestBody UserInfo userInfo);

}
