package com.study.demo;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.study.demo.config.StreamMqConfig.StreamMessageConsumer;
import com.study.demo.config.StreamMqConfig.StreamMessageProducer;
import com.study.demo.entity.UserInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@Autowired
	private StreamMessageProducer streamMessageProducer;

	@Autowired
	private StreamMessageConsumer streamMessageConsumer;

	@Test
	public void test() throws InterruptedException {
//		streamMessageConsumer.input().subscribe(new MessageHandler() {
//
//			@Override
//			public void handleMessage(Message<?> message) throws MessagingException {
//
//				System.out.println(1);
//				MessageBuilder.fromMessage(message).build();
//			}
//		});
//		
//		streamMessageConsumer.input1().subscribe(new MessageHandler() {
//
//			@Override
//			public void handleMessage(Message<?> message) throws MessagingException {
//
//				System.out.println(2);
//				MessageBuilder.fromMessage(message).build();
//			}
//		});
		
		MessageChannel channel=streamMessageProducer.produce();
		channel.send(MessageBuilder.withPayload(new UserInfo(123L, "中国强")).build());

		while (true) {
			TimeUnit.SECONDS.sleep(2);
			
		}
	}

}
