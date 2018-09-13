package com.webcabi.snowlinebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.webcabi"})
public class SNowLineBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SNowLineBotApplication.class, args);
	}
}
