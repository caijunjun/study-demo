package com.study.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.study.demo.entity.UserInfo;

@Component
public class TestListener {

	private Logger logger = LoggerFactory.getLogger(TestListener.class);

	private int count = 1;

	@StreamListener("demo_input")
	public void receive(UserInfo payload) {
		logger.info("Received payload {} count {}", payload, count);
		if (count == 3) {
			count = 1;
			throw new AmqpRejectAndDontRequeueException("tried 3 times failed, send to dlq!");
		} else {
			count++;
			throw new RuntimeException("Message consumer failed!");
		}
	}

	@StreamListener("demo_input1")
	public void receive1(UserInfo payload) {
		logger.info("demo_input1{} ", payload);
		throw new RuntimeException("Message consumer failed!");
	}

	/**
	 * 消息消费失败的降级处理逻辑
	 *
	 * @param message
	 */
	@ServiceActivator(inputChannel = "greetings.Service.errors")
	public void error(Message<?> message) {
		logger.info("Message consumer failed, call fallback!");
	}

}
