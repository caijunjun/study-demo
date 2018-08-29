package com.example.demo.routing;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.hander.UserHandler;

/**
 * 
 * @author caijunjun
 * @date 2018年8月29日
 */
@Configuration
public class RoutingConfiguration {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {

		
		return RouterFunctions.route(POST("/user"), userHandler::addUser)
				.andRoute(GET("/user/delete/{id}"), userHandler::deleteUser)
				.andRoute(POST("/user/edit"), userHandler::editUser)
				.andRoute(GET("/user/{id}"), userHandler::getUser); }

}
