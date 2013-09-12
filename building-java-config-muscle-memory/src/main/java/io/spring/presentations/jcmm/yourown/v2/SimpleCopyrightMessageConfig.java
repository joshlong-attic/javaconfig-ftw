package io.spring.presentations.jcmm.yourown.v2;

import io.spring.presentations.jcmm.CustomService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleCopyrightMessageConfig {

	@Bean
	public CustomService myCustomService() {
		CustomService customService = new CustomService();
		customService.setMessage("(c) Long & Webb Ltd 2013");
		return customService;
	}

}
