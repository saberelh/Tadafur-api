// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/analytics/AnalyticsController.java
package com.project.Tadafur_api.infrastructure.web.controller.analytics;

import com.project.Tadafur_api.application.dto.analytics.ProjectSpendingTrendDto;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Analytics & BI", description = "Endpoints for business intelligence and decision-making.")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * REFACTORED ENDPOINT: Gets a health summary for strategies, with an optional owner filter.
     * - /api/analytics/strategic-health -> returns health for ALL strategies.
     * - /api/analytics/strategic-health?ownerId=123 -> returns health for owner 123's strategies.
     */
    @GetMapping("/strategic-health")
    @Operation(summary = "Get Health Summary for Strategies (Optional Owner Filter)",
            description = "Returns a list of health summaries (KPIs). If ownerId is provided, it filters for that owner.")
    public ResponseEntity<List<StrategicHealthDto>> getStrategicHealths(
            @Parameter(description = "Optional: Filter by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "The time period for the analysis (e.g., 'current'). Currently a placeholder.")
            @RequestParam(defaultValue = "current") String dateRange,
            @Parameter(description = "Language code for the strategy names.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received request for strategic health summaries with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(analyticsService.getStrategicHealths(Optional.ofNullable(ownerId), dateRange, lang));
    }

    @GetMapping("/trends/spending-vs-plan")
    @Operation(summary = "Get Spending vs. Plan Trend for Projects",
            description = "Returns time-series data comparing cumulative actual spend vs. cumulative planned spend. " +
                    "Can be filtered by a specific ownerId or a single projectId.")
    public ResponseEntity<List<ProjectSpendingTrendDto>> getSpendingVsPlan(
            @Parameter(description = "Optional: Filter by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Optional: Filter for a single project by its ID.")
            @RequestParam(required = false) Long projectId,
            @Parameter(description = "Language code for project names.")
            @RequestParam(defaultValue = "en") String lang) {

        return ResponseEntity.ok(analyticsService.getSpendingVsPlanTrend(Optional.ofNullable(ownerId), Optional.ofNullable(projectId), lang));
    }
}