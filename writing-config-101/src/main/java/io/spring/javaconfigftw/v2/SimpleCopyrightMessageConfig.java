package io.spring.javaconfigftw.v2;

import io.spring.javaconfigftw.SimpleMessageBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleCopyrightMessageConfig {

	@Bean
	public SimpleMessageBean copyrightMessageBean() {
		return new SimpleMessageBean("(c) Long & Webb Ltd 2013");
	}

}
