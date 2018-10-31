package com.study.web.service.user.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.common.util.IdUtil;
import com.study.web.dao.UserInfoMapper;
import com.study.web.entity.UserInfo;
import com.study.web.handler.HandlerChain;
import com.study.web.handler.user.UserContext;
import com.study.web.handler.user.UserHandler;
import com.study.web.handler.user.UserResult;
import com.study.web.service.user.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService,UserHandler {

	@Autowired
	private UserInfoMapper userInfoMapper;

	
	@Override
	public UserInfo getUserInfo(long uid) {
		return userInfoMapper.selectByPrimaryKey(uid);
	}

	@Transactional
	@Override
	public UserInfo addUserInfo(UserInfo userInfo) {
		userInfoMapper.insert(userInfo);
		return userInfo;
	}

	@Override
	public UserResult handler(UserContext context, UserResult result) {
		// TODO Auto-generated method stub
		return new UserResult();
	}

	@Override
	public int getHandlerOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}


}
