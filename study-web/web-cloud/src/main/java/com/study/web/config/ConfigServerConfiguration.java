package com.study.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(value=EurekaServerConfiguration.class)
@Configuration
@EnableConfigServer
public class ConfigServerConfiguration {

}
