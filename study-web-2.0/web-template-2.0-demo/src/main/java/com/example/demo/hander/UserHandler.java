package com.example.demo.hander;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class UserHandler {

	@Autowired
	private UserRepository userRepository;

	public Mono<ServerResponse> addUser(ServerRequest request) {
		return ServerResponse.ok().build(request.bodyToMono(User.class).doOnNext(user -> {
			userRepository.save(user);
		}).then());
	}

	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		// ...
		return Mono.empty();
	}

	public Mono<ServerResponse> editUser(ServerRequest request) {
		// ...
		return Mono.empty();
	}

	public Mono<ServerResponse> getUser(ServerRequest request) {
		return Mono.create(monoSink -> {
			monoSink.success();
		});
		// ...
	}

	public Mono<ServerResponse> listUser(ServerRequest request) {
		
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(Flux.fromIterable(userRepository.findAll()),
				User.class);
	}
}
