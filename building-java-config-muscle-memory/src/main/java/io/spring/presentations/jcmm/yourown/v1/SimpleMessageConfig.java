package io.spring.presentations.jcmm.yourown.v1;

import io.spring.presentations.jcmm.CustomService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleMessageConfig {

	protected String getMessage() {
		return "Hello";
	}

	@Bean
	public CustomService myCustomService() {
		CustomService customService = new CustomService();
		customService.setMessage(getMessage());
		return customService;
	}

}
