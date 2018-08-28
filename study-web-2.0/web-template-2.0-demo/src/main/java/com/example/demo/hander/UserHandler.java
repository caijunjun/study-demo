package com.example.demo.hander;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	public Mono<ServerResponse> getUser(ServerRequest request) {
		return Mono.create(monoSink -> {
			monoSink.success();
		});
		// ...
	}
	public boolean test(ServerRequest request) {
		return true;
	}

	public Mono<ServerResponse> getUserCustomers(ServerRequest request) {
		// ...
		return Mono.empty();
	}

	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		// ...
		return Mono.empty();
	}
}
