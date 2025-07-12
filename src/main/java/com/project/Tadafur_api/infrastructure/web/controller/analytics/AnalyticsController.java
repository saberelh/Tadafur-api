// =================================================================================
// STEP 2: UPDATE THE ANALYTICS CONTROLLER
// We will add a new endpoint to expose the new service method.
// =================================================================================

// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/analytics/AnalyticsController.java
package com.project.Tadafur_api.infrastructure.web.controller.analytics;

import com.project.Tadafur_api.application.dto.analytics.StrategicHealthDto;
import com.project.Tadafur_api.application.service.analytics.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Analytics & BI", description = "Endpoints for business intelligence and decision-making.")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * NEW ENDPOINT: Gets a health summary for ALL strategies.
     */
    @GetMapping("/strategic-health")
    @Operation(summary = "Get Health Summary for All Strategies",
            description = "Returns a list containing a health summary (KPIs) for every strategy in the system.")
    public ResponseEntity<List<StrategicHealthDto>> getAllStrategicHealths(
            @Parameter(description = "The time period for the analysis (e.g., 'current'). Currently a placeholder.", example = "current")
            @RequestParam(defaultValue = "current") String dateRange,
            @Parameter(description = "Language code for the strategy names.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received request for all strategic health summaries.");
        return ResponseEntity.ok(analyticsService.getAllStrategicHealths(dateRange, lang));
    }

    @GetMapping("/strategic-health/{strategyId}")
    @Operation(summary = "Get Overall Strategic Health for a Single Strategy",
            description = "Calculates high-level KPIs for a given strategy, including progress, budget, and schedule health.")
    public ResponseEntity<StrategicHealthDto> getStrategicHealth(
            @Parameter(description = "The ID of the root strategy.", example = "1")
            @PathVariable Long strategyId,
            @Parameter(description = "The time period for the analysis. Currently a placeholder.", example = "current")
            @RequestParam(defaultValue = "current") String dateRange,
            @Parameter(description = "Language code for the strategy name.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received request for strategic health of strategy ID: {}", strategyId);
        return ResponseEntity.ok(analyticsService.getStrategicHealth(strategyId, dateRange, lang));
    }
}
