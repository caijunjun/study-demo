package com.study.demo.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import com.study.demo.config.StreamMqConfig.StreamMessageConsumer;
import com.study.demo.config.StreamMqConfig.StreamMessageProducer;

@EnableBinding({ StreamMessageProducer.class, StreamMessageConsumer.class })
public class StreamMqConfig {

	public interface StreamMessageConsumer {

		@Input("demo_input")
		SubscribableChannel input();
		
		@Input("demo_input1")
		SubscribableChannel input1();
	}

	public interface StreamMessageProducer {

		@Output("demo_output")
		MessageChannel produce();


	}
}
