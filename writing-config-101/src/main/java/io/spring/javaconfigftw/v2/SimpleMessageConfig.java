package io.spring.javaconfigftw.v2;

import io.spring.javaconfigftw.SimpleMessageBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleMessageConfig {

	@Bean
	public SimpleMessageBean simpleMessageBean() {
		return new SimpleMessageBean("Hello");
	}

}
