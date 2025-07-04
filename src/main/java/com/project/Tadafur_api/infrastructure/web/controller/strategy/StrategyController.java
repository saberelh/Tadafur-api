// File: infrastructure/web/controller/strategy/StrategyController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.service.strategy.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Strategy REST Controller
 * Provides endpoints for Strategy management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/strategies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Strategy Management", description = "API endpoints for managing strategies")
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * Get all strategies with pagination
     * GET /api/v1/strategies
     */
    @GetMapping
    @Operation(summary = "Get all strategies", description = "Retrieve all active strategies with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getAllStrategies(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("GET /api/v1/strategies - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Map<String, Object> response = strategyService.getAllStrategies(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    /**
     * Get strategy by ID
     * GET /api/v1/strategies/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get strategy by ID", description = "Retrieve a specific strategy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved strategy"),
            @ApiResponse(responseCode = "404", description = "Strategy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StrategyResponseDto> getStrategyById(
            @Parameter(description = "Strategy ID", required = true, example = "1")
            @PathVariable Long id) {

        log.info("GET /api/v1/strategies/{}", id);

        StrategyResponseDto strategy = strategyService.getStrategyById(id);
        return ResponseEntity.ok(strategy);
    }

    /**
     * Search strategies
     * GET /api/v1/strategies/search?query={query}
     */
    @GetMapping("/search")
    @Operation(summary = "Search strategies", description = "Search strategies by name, description, or vision")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results"),
            @ApiResponse(responseCode = "400", description = "Invalid search query"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> searchStrategies(
            @Parameter(description = "Search query", required = true, example = "Digital Transformation")
            @RequestParam String query,

            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/strategies/search - query: {}, page: {}, size: {}", query, page, size);

        Map<String, Object> response = strategyService.searchStrategies(query, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get strategies by owner
     * GET /api/v1/strategies/by-owner/{ownerId}
     */
    @GetMapping("/by-owner/{ownerId}")
    @Operation(summary = "Get strategies by owner", description = "Retrieve all strategies owned by a specific owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies"),
            @ApiResponse(responseCode = "404", description = "Owner not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getStrategiesByOwner(
            @Parameter(description = "Owner ID", required = true, example = "100")
            @PathVariable Long ownerId,

            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/strategies/by-owner/{} - page: {}, size: {}", ownerId, page, size);

        Map<String, Object> response = strategyService.getStrategiesByOwner(ownerId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get active strategies
     * GET /api/v1/strategies/active
     */
    @GetMapping("/active")
    @Operation(summary = "Get active strategies", description = "Retrieve all currently active strategies based on timeline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active strategies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getActiveStrategies(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/strategies/active - page: {}, size: {}", page, size);

        Map<String, Object> response = strategyService.getActiveStrategies(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get strategies by timeline
     * GET /api/v1/strategies/by-timeline?from={date}&to={date}
     */
    @GetMapping("/by-timeline")
    @Operation(summary = "Get strategies by timeline", description = "Retrieve strategies within a specific timeline range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies"),
            @ApiResponse(responseCode = "400", description = "Invalid date parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getStrategiesByTimeline(
            @Parameter(description = "From date (yyyy-MM-dd)", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @Parameter(description = "To date (yyyy-MM-dd)", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/strategies/by-timeline - from: {}, to: {}, page: {}, size: {}",
                from, to, page, size);

        Map<String, Object> response = strategyService.getStrategiesByTimeline(from, to, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get strategy summary
     * GET /api/v1/strategies/summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get strategy summary", description = "Retrieve summary statistics for all strategies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summary"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getStrategySummary() {
        log.info("GET /api/v1/strategies/summary");

        Map<String, Object> summary = strategyService.getStrategySummary();
        return ResponseEntity.ok(summary);
    }
}