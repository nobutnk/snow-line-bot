package com.webcabi.snowlinebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages={"com.webcabi"})
@PropertySource("classpath:linebot.properties")
public class SNowLineBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SNowLineBotApplication.class, args);
	}
}
