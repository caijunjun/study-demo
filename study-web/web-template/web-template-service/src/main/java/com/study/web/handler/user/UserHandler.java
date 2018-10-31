package com.study.web.handler.user;

import com.study.web.entity.UserInfo;
import com.study.web.handler.Handler;

public interface UserHandler extends Handler<UserContext, UserResult> {

	UserInfo getUserInfo();
}
