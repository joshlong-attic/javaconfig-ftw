package io.spring.javaconfigftw.v1;

import io.spring.javaconfigftw.SimpleMessageBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleMessageConfig {

	protected String getMessage() {
		return "Hello";
	}

	@Bean
	public SimpleMessageBean simpleMessageBean() {
		return new SimpleMessageBean(getMessage());
	}

}
