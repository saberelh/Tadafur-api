package com.project.Tadafur_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class ControllerStatusController {

    @GetMapping("/check-files")
    public String checkControllerFiles() {
        StringBuilder result = new StringBuilder();
        result.append("Controller File Check:<br>");

        // Check if strategy controller class exists
        try {
            Class.forName("com.project.Tadafur_api.domain.strategy.controller.hierarchy.StrategyController");
            result.append("✅ StrategyController class found<br>");
        } catch (ClassNotFoundException e) {
            result.append("❌ StrategyController class NOT found<br>");
        }

        try {
            Class.forName("com.project.Tadafur_api.domain.strategy.controller.hierarchy.PerspectiveController");
            result.append("✅ PerspectiveController class found<br>");
        } catch (ClassNotFoundException e) {
            result.append("❌ PerspectiveController class NOT found<br>");
        }

        try {
            Class.forName("com.project.Tadafur_api.domain.strategy.service.hierarchy.StrategyService");
            result.append("✅ StrategyService class found<br>");
        } catch (ClassNotFoundException e) {
            result.append("❌ StrategyService class NOT found<br>");
        }

        try {
            Class.forName("com.project.Tadafur_api.shared.common.entity.BaseEntity");
            result.append("✅ BaseEntity class found<br>");
        } catch (ClassNotFoundException e) {
            result.append("❌ BaseEntity class NOT found<br>");
        }

        return result.toString();
    }

    @GetMapping("/component-scan-test")
    public String componentScanTest() {
        return "Component scanning is working for this package: com.project.Tadafur_api";
    }
}