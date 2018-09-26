package com.study.web.handler.user;

import com.study.web.handler.CoreContext;
import com.study.web.handler.CoreHandler;
import com.study.web.handler.CoreResult;

public interface UserHandler extends CoreHandler{

	CoreResult handler(CoreContext coreContext, CoreResult coreResult);
}
