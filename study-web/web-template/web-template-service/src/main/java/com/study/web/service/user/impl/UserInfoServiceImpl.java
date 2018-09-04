package com.study.web.service.user.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.util.IdUtil;
import com.study.web.dao.UserInfoMapper;
import com.study.web.entity.UserInfo;
import com.study.web.service.user.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public UserInfo getUserInfo(long uid) {
		return userInfoMapper.selectByPrimaryKey(uid);
	}

	@Override
	public UserInfo addUserInfo(UserInfo userInfo) {
		userInfo.setId(IdUtil.getLongId());
		userInfo.setCreateTime(new Date());
		userInfoMapper.insert(userInfo);
		return userInfo;
	}

}
