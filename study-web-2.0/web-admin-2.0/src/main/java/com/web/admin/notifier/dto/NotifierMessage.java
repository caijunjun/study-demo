package com.web.admin.notifier.dto;

import java.io.Serializable;

public class NotifierMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5106756453478025129L;
	
	private String msgContent;
	
	
	public NotifierMessage(String msgContent) {
		super();
		this.msgContent = msgContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Override
	public String toString() {
		return "NotifierMessage [msgContent=" + msgContent + "]";
	}
	
	

}
