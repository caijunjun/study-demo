package com.web.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.web.gateway.bean.BatchRedisTemplate;

@Configuration
public class RedisAutoConfiguration {

	private RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
	private RedisSerializer<Object> genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

	@Bean
	public BatchRedisTemplate batchRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
		return new BatchRedisTemplate(redisTemplate);
	}

	@Bean
	public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
		return redisTemplate;
	}

}
