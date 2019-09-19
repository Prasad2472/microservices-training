package com.boot.monolithic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.boot.monolithic.controller" })
public class MonolothicappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonolothicappApplication.class, args);
	}

}
