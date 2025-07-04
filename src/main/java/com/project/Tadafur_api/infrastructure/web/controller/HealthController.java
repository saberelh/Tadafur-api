// File: infrastructure/web/controller/HealthController.java
package com.project.Tadafur_api.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired(required = false)
    private DataSource dataSource;

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        // Test database connection
        if (dataSource != null) {
            try {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                response.put("database", "Connected");
            } catch (Exception e) {
                response.put("database", "Error: " + e.getMessage());
            }
        } else {
            response.put("database", "No DataSource configured");
        }

        return response;
    }

    @GetMapping("/simple")
    public String simple() {
        return "OK";
    }
}