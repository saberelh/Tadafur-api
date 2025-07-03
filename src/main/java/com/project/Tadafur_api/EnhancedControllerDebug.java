package com.project.Tadafur_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/debug")
public class EnhancedControllerDebug {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/spring-beans")
    public String listSpringBeans() {
        StringBuilder result = new StringBuilder();
        result.append("Spring Beans Check:<br><br>");

        // Check for Strategy beans
        String[] strategyBeans = applicationContext.getBeanNamesForType(Object.class);

        result.append("Looking for Strategy-related beans:<br>");
        Arrays.stream(strategyBeans)
                .filter(name -> name.toLowerCase().contains("strategy"))
                .forEach(name -> result.append("✅ Found: ").append(name).append("<br>"));

        result.append("<br>Looking for Service beans:<br>");
        Arrays.stream(strategyBeans)
                .filter(name -> name.toLowerCase().contains("service"))
                .forEach(name -> result.append("✅ Found: ").append(name).append("<br>"));

        result.append("<br>Looking for Controller beans:<br>");
        Arrays.stream(strategyBeans)
                .filter(name -> name.toLowerCase().contains("controller"))
                .forEach(name -> result.append("✅ Found: ").append(name).append("<br>"));

        result.append("<br>Looking for Repository beans:<br>");
        Arrays.stream(strategyBeans)
                .filter(name -> name.toLowerCase().contains("repository"))
                .forEach(name -> result.append("✅ Found: ").append(name).append("<br>"));

        return result.toString();
    }

    @GetMapping("/check-strategy-components")
    public String checkStrategyComponents() {
        StringBuilder result = new StringBuilder();
        result.append("Strategy Component Check:<br><br>");

        // Check if specific classes exist
        try {
            applicationContext.getBean("strategyController");
            result.append("✅ StrategyController Bean found<br>");
        } catch (Exception e) {
            result.append("❌ StrategyController Bean NOT found: ").append(e.getMessage()).append("<br>");
        }

        try {
            applicationContext.getBean("strategyService");
            result.append("✅ StrategyService Bean found<br>");
        } catch (Exception e) {
            result.append("❌ StrategyService Bean NOT found: ").append(e.getMessage()).append("<br>");
        }

        try {
            applicationContext.getBean("strategyRepository");
            result.append("✅ StrategyRepository Bean found<br>");
        } catch (Exception e) {
            result.append("❌ StrategyRepository Bean NOT found: ").append(e.getMessage()).append("<br>");
        }

        try {
            applicationContext.getBean("perspectiveController");
            result.append("✅ PerspectiveController Bean found<br>");
        } catch (Exception e) {
            result.append("❌ PerspectiveController Bean NOT found: ").append(e.getMessage()).append("<br>");
        }

        try {
            applicationContext.getBean("perspectiveService");
            result.append("✅ PerspectiveService Bean found<br>");
        } catch (Exception e) {
            result.append("❌ PerspectiveService Bean NOT found: ").append(e.getMessage()).append("<br>");
        }

        return result.toString();
    }

    @GetMapping("/check-packages")
    public String checkPackageStructure() {
        StringBuilder result = new StringBuilder();
        result.append("Package Structure Check:<br><br>");

        // Check main classes
        String[] classesToCheck = {
                "com.project.Tadafur_api.domain.strategy.controller.hierarchy.StrategyController",
                "com.project.Tadafur_api.domain.strategy.service.hierarchy.StrategyService",
                "com.project.Tadafur_api.domain.strategy.repository.StrategyRepository",
                "com.project.Tadafur_api.domain.strategy.entity.Strategy",
                "com.project.Tadafur_api.domain.strategy.dto.response.StrategyResponseDto",
                "com.project.Tadafur_api.shared.common.entity.BaseEntity"
        };

        for (String className : classesToCheck) {
            try {
                Class.forName(className);
                result.append("✅ ").append(className).append(" found<br>");
            } catch (ClassNotFoundException e) {
                result.append("❌ ").append(className).append(" NOT found<br>");
            }
        }

        return result.toString();
    }
}