package com.web.admin.notifier.service;

import com.web.admin.notifier.dto.NotifierMessage;

public interface NotifierService {

	/**
	 * 发送通知
	 * @param notifierMessage
	 * @return
	 */
	boolean send(NotifierMessage notifierMessage);
}
