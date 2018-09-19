package com.web.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.web.gateway.bean.BatchRedisTemplate;
import com.web.gateway.filter.AuthorizeGatewayFilterFactory;

@Configuration
public class FilterAutoConfiguration {

	@Bean
	public AuthorizeGatewayFilterFactory tokenGatewayFilterFactory(BatchRedisTemplate batchRedisTemplate) {
		return new AuthorizeGatewayFilterFactory(batchRedisTemplate);
	}
}
