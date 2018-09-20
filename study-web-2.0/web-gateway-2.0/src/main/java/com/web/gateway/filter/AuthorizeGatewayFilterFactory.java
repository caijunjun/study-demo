package com.web.gateway.filter;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.web.gateway.bean.BatchRedisTemplate;

import reactor.core.publisher.Mono;

public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

	private Logger logger = LoggerFactory.getLogger(AuthorizeGatewayFilterFactory.class);

	private BatchRedisTemplate batchRedisTemplate;

	public AuthorizeGatewayFilterFactory(BatchRedisTemplate batchRedisTemplate) {
		super(Config.class);
		logger.info("Loaded GatewayFilterFactory [Authorize] with batchRedisTemplate");
		this.batchRedisTemplate = batchRedisTemplate;
	}

	@Override
	public List<String> shortcutFieldOrder() {
		// 存在这个字段则实例化，反之不实例化
		return Arrays.asList(NAME_KEY);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			String token = request.getHeaders().getFirst(config.getTokenHeader());

			// 令牌为空
			if (token == null || token.length() == 0) {
//				return noAuth(response);
			}
			// 校验令牌
			boolean checkToken = checkToken(config.getTokenKeyPrefix() + token);
			// 令牌校验不通过
			if (!checkToken) {
//				return noAuth(response);
			}

			return chain.filter(exchange);

		};

	}

	private boolean checkToken(String tokenKey) {
		return batchRedisTemplate.exists(tokenKey);
	}

	private Mono<Void> noAuth(ServerHttpResponse response) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

	public static class Config {
		/**
		 * 令牌头名称
		 */
		private String tokenHeader;

		/**
		 * 令牌缓存key前缀
		 */
		private String tokenKeyPrefix;
		

		public String getTokenHeader() {
			return tokenHeader;
		}

		public void setTokenHeader(String tokenHeader) {
			this.tokenHeader = tokenHeader;
		}

		public String getTokenKeyPrefix() {
			return tokenKeyPrefix;
		}

		public void setTokenKeyPrefix(String tokenKeyPrefix) {
			this.tokenKeyPrefix = tokenKeyPrefix;
		}

	}

}
