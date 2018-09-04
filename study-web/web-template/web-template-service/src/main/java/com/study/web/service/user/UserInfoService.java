package com.study.web.service.user;

import com.study.web.entity.UserInfo;

public interface UserInfoService {

	/**
	 * 获取用户信息
	 * @param uid
	 * @return
	 */
	UserInfo getUserInfo(long uid);

	/**
	 * 创建用户信息
	 * @param userInfo
	 * @return
	 */
	UserInfo addUserInfo(UserInfo userInfo);
}
