package com.study.web.feign;

import org.springframework.cloud.netflix.feign.FeignClient;

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
