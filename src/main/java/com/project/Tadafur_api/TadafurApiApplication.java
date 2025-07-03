package com.project.Tadafur_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "com.project.Tadafur_api.domain.strategy.repository"
})
@EnableJpaAuditing
@ComponentScan(basePackages = {
        "com.project.Tadafur_api"
})
public class TadafurApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TadafurApiApplication.class, args);
    }
}