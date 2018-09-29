package com.study.web.handler.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.web.dao.UserInfoMapper;

@Service
public class UserHandler2 implements UserHandler {

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Override
	public int getOrder() {
		return 100;
	}

	@Transactional
	@Override
	public UserResult handler(UserContext context, UserResult result) {
		context.getUserInfo().setModifyTime(new Date());
		userInfoMapper.updateByPrimaryKeySelective(context.getUserInfo());
		return null;
	}


}
