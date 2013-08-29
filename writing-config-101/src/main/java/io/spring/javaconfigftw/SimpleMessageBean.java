package io.spring.javaconfigftw;

import javax.annotation.PostConstruct;

public class SimpleMessageBean {

	private String message;

	public SimpleMessageBean(String message) {
		this.message= message;
	}

	@PostConstruct
	public void displayMessage() {
		System.out.println("*************************************************");
		System.out.println(this.message);
		System.out.println("*************************************************");
	}

}
