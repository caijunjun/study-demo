package com.study.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class RefreshController {

	@Value("${spring.datasource.druid.stat-view-servlet.login-username}")
	private String name;
	
	@RequestMapping("/getName")
	public String getName() {
		return name;
	}
}
