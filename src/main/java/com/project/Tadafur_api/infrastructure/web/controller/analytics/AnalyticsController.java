package com.project.Tadafur_api.infrastructure.web.controller.analytics;

import com.project.Tadafur_api.application.dto.analytics.ProjectSpendingDetailsDto;
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
     * NEW: Endpoint to get detailed spending information for projects.
     */
    @GetMapping("/trends/spending-details")
    @Operation(summary = "Get Detailed Spending Information for Projects",
            description = "Returns a list of projects, each with its own summary totals and a full list of all payment transactions.")
    public ResponseEntity<List<ProjectSpendingDetailsDto>> getSpendingDetails(
            @Parameter(description = "Optional: The ID of the project to filter by.", example = "1")
            @RequestParam(required = false) Long id,
            @Parameter(description = "Optional: The ID of the owner to filter by.", example = "101")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for the project names.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {

        List<ProjectSpendingDetailsDto> spendingDetails = analyticsService.getSpendingDetails(id, ownerId, lang);
        return ResponseEntity.ok(spendingDetails);
    }

    /**
     * UNCHANGED: Gets a health summary for ALL strategies.
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

    /**
     * UNCHANGED: Gets the overall strategic health for a SINGLE strategy.
     */
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