package com.study.web.handler.user;

import com.study.web.entity.UserInfo;
import com.study.web.handler.Context;

public class UserContext implements Context{

	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public boolean isOpenTransaction() {
		return true;
	}
	
	
}
