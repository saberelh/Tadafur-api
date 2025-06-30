package com.project.Tadafur_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Tadafur API! 🚀 Database connection ready for testing!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Tadafur API! 🌍 Ready to connect to PostgreSQL!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test endpoints available: /api/test/db-connection ✅";
    }
}