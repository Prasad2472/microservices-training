package com.boot.microservice.registrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = { "com.boot.monolithic.controller" })
@EnableEurekaClient
public class RegistrationRibbonStrategy {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationRibbonStrategy.class, args);
	}

}
