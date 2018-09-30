package com.study.web.handler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.web.dao.UserInfoMapper;

@Service
public class UserHandler1 implements UserHandler {

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Override
	public UserResult handler(UserContext context, UserResult result) {
		UserHandlerProxy.currentChainInfoHolder().setResult(new UserResult());
		userInfoMapper.insert(context.getUserInfo());
		return new UserResult();
	}

	@Override
	public int getHandlerOrder() {
		// TODO Auto-generated method stub
		return 0;
	}


}
