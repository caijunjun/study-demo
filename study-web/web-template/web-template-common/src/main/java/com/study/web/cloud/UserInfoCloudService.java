package com.study.web.cloud;

import com.study.web.entity.UserInfo;

public interface UserInfoCloudService {

	UserInfo getUserInfo(long uid);

	UserInfo addUserInfo(UserInfo userInfo);

}
