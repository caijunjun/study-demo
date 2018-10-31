package com.study.web.handler.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.web.dao.UserInfoMapper;
import com.study.web.entity.UserInfo;

@Service
public class UserHandler2 implements UserHandler {

	@Autowired
	private UserInfoMapper userInfoMapper;
	

	@Override
	public UserResult handler(UserContext context, UserResult result) {
		context.getUserInfo().setModifyTime(new Date());
		userInfoMapper.updateByPrimaryKeySelective(context.getUserInfo());
		return null;
	}

	@Override
	public int getHandlerOrder() {
		return 100;
	}

	@Override
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}


}
