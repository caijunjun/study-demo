package com.web.admin.notifier.service.impl;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.web.admin.notifier.dto.NotifierMessage;
import com.web.admin.notifier.service.NotifierService;
import com.web.admin.util.JsonNodeUtil;

@Service
public class NotifierServiceImpl implements NotifierService {

	private String corpid = "";

	private String corpsecret = "";

	private String chatid = "";

	private String gettokenUrl = "https://oapi.dingtalk.com/gettoken?corpid={0}&corpsecret={1}";

	private String sendUrl = "https://oapi.dingtalk.com/chat/send?access_token={0}";

	@Value("${spring.profiles.active}")
	private String active;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean send(NotifierMessage notifierMessage) {

		ObjectNode tokenNode = restTemplate.exchange(MessageFormat.format(gettokenUrl, corpid,corpsecret), HttpMethod.GET, null, ObjectNode.class).getBody();
		String accessToken = tokenNode.get("access_token").asText();

		ObjectNode body = JsonNodeUtil.data()//
				.put("msgtype", "text")//
				.put("chatid", chatid)//
				.put("text", JsonNodeUtil.data().put("content", "["+active+"]"+notifierMessage.getMsgContent()).toString());

		HttpEntity<ObjectNode> httpEntity = new HttpEntity<>(body);

		restTemplate.exchange(MessageFormat.format(sendUrl, accessToken), HttpMethod.POST, httpEntity, ObjectNode.class)
				.getBody();

		return true;
	}

}
