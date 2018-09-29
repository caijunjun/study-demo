package com.study.web.handler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.web.dao.UserInfoMapper;

@Service
public class UserHandler1 implements UserHandler {

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public UserResult handler(UserContext context, UserResult result) {
		userInfoMapper.insert(context.getUserInfo());
		return null;
	}


}
