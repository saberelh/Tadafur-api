package com.project.Tadafur_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.project.Tadafur_api",
		"com.project.Tadafur_api.domain.strategy",
		"com.project.Tadafur_api.shared"
})
public class TadafurApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TadafurApiApplication.class, args);
	}
}