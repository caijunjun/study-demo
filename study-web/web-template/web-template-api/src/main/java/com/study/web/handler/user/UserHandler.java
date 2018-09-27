package com.study.web.handler.user;

import com.study.web.handler.core.CoreContext;
import com.study.web.handler.core.CoreHandler;
import com.study.web.handler.core.CoreResult;

public interface UserHandler extends CoreHandler{

	CoreResult handler(CoreContext coreContext, CoreResult coreResult);
}
