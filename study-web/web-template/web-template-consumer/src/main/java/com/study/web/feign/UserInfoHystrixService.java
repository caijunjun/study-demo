package com.study.web.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.web.cloud.UserInfoCloudService;
import com.study.web.entity.UserInfo;
import com.study.web.feign.UserInfoHystrixService.UserInfoHystrix;

/**
 * 微服务客户端
 * 
 * @author caijunjun
 * @date 2018年9月3日
 */
@FeignClient(name = "eureka-service-template", path = "/web", fallback = UserInfoHystrix.class)
public interface UserInfoHystrixService extends UserInfoCloudService {

	@RequestMapping(value = "/userInfo/cloud/get", method = RequestMethod.GET)
	UserInfo getUserInfo(@RequestParam("uid") long uid);

	@RequestMapping(value = "/userInfo/cloud/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	UserInfo addUserInfo(@RequestBody UserInfo userInfo);

	static class UserInfoHystrix implements UserInfoCloudService {
		@Override
		public UserInfo getUserInfo(long uid) {
			return null;
		}

		@Override
		public UserInfo addUserInfo(UserInfo userInfo) {
			return null;
		}
	}
}
