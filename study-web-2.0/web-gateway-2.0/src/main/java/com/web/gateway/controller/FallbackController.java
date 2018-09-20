package com.web.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
	@GetMapping(value = "/fallback")
	public Mono<Void> fallback() {
		return Mono.empty();
	}
}