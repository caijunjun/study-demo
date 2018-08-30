package com.example.demo.controller;

import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping("/create")
	public Mono<Void> create(@RequestBody Publisher<User> publisher) {
		return Mono.from(publisher).doOnNext(user->{
			System.out.println(user);
		}).then();
	}
}
