package com.project.Tadafur_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.project.Tadafur_api.application",
        "com.project.Tadafur_api.domain",
        "com.project.Tadafur_api.infrastructure",
        "com.project.Tadafur_api.shared"
})
@EnableJpaRepositories(basePackages = "com.project.Tadafur_api.domain.strategy.repository")
public class TadafurApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TadafurApiApplication.class, args);
    }
}