package com.project.Tadafur_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/strategy-test")
public class StrategyTestController {

    @GetMapping("/hello")
    public String hello() {
        return "Strategy Controller is working! 🎯";
    }

    @GetMapping("/test-endpoint")
    public String testEndpoint() {
        return "Strategy domain endpoint accessible!";
    }
}