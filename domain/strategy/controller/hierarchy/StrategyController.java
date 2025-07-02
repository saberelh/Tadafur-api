package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.StrategyResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategy/hierarchy/strategies")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Strategy Management", description = "Strategic Planning Hierarchy - Strategy Level Operations")
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping
    @Operation(
            summary = "Get all active strategies",
            description = "Retrieves a paginated list of all active strategies with basic information and aggregated metrics"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies")
    public ResponseEntity<Page<StrategyResponseDto>> getAllStrategies(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all strategies - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<StrategyResponseDto> strategies = strategyService.getAllActiveStrategies(pageable);

        log.info("Retrieved {} strategies out of {} total",
                strategies.getNumberOfElements(), strategies.getTotalElements());

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get strategy by ID",
            description = "Retrieves detailed information about a specific strategy including timeline status and budget metrics"
    )
    @ApiResponse(responseCode = "200", description = "Strategy found and returned")
    @ApiResponse(responseCode = "404", description = "Strategy not found")
    public ResponseEntity<StrategyResponseDto> getStrategyById(
            @Parameter(description = "Strategy ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get strategy by ID: {}", id);

        StrategyResponseDto strategy = strategyService.getStrategyById(id);

        log.info("Successfully retrieved strategy: {} ({})",
                strategy.getDisplayName(), strategy.getId());

        return ResponseEntity.ok(strategy);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get strategies by owner",
            description = "Retrieves all strategies owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies for owner")
    public ResponseEntity<List<StrategyResponseDto>> getStrategiesByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get strategies by owner: {}", ownerId);

        List<StrategyResponseDto> strategies = strategyService.getStrategiesByOwner(ownerId);

        log.info("Retrieved {} strategies for owner: {}", strategies.size(), ownerId);

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search strategies",
            description = "Search strategies by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<StrategyResponseDto>> searchStrategies(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search strategies with query: '{}', page: {}, size: {}",
                query, pageable.getPageNumber(), pageable.getPageSize());

        Page<StrategyResponseDto> strategies = strategyService.searchStrategies(query, pageable);

        log.info("Search returned {} strategies for query: '{}'",
                strategies.getTotalElements(), query);

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/active-on-date")
    @Operation(
            summary = "Get strategies active on specific date",
            description = "Retrieves all strategies that are active (within timeline) on the specified date"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active strategies")
    public ResponseEntity<List<StrategyResponseDto>> getActiveStrategiesOnDate(
            @Parameter(description = "Date to check (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get strategies active on date: {}", date);

        List<StrategyResponseDto> strategies = strategyService.getActiveStrategiesOnDate(date);

        log.info("Found {} strategies active on date: {}", strategies.size(), date);

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/timeline-range")
    @Operation(
            summary = "Get strategies by timeline range",
            description = "Retrieves strategies that start within the specified date range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies in timeline range")
    public ResponseEntity<List<StrategyResponseDto>> getStrategiesByTimelineRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        log.info("REST request to get strategies by timeline range: {} to {}", fromDate, toDate);

        List<StrategyResponseDto> strategies = strategyService.getStrategiesByTimelineRange(fromDate, toDate);

        log.info("Found {} strategies in timeline range: {} to {}", strategies.size(), fromDate, toDate);

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/year/{year}")
    @Operation(
            summary = "Get strategies by year",
            description = "Retrieves all strategies that start in the specified year"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies for year")
    public ResponseEntity<List<StrategyResponseDto>> getStrategiesByYear(
            @Parameter(description = "Year", required = true)
            @PathVariable @Min(2000) int year) {

        log.info("REST request to get strategies by year: {}", year);

        List<StrategyResponseDto> strategies = strategyService.getStrategiesByYear(year);

        log.info("Found {} strategies for year: {}", strategies.size(), year);

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/recent")
    @Operation(
            summary = "Get recent strategies",
            description = "Retrieves the most recently created strategies"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent strategies")
    public ResponseEntity<List<StrategyResponseDto>> getRecentStrategies(
            @Parameter(description = "Maximum number of strategies to return", required = false)
            @RequestParam(defaultValue = "10") @Min(1) int limit) {

        log.info("REST request to get recent strategies, limit: {}", limit);

        List<StrategyResponseDto> strategies = strategyService.getRecentStrategies(limit);

        log.info("Retrieved {} recent strategies", strategies.size());

        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/summary")
    @Operation(
            summary = "Get strategy summary",
            description = "Retrieves aggregated statistics about all active strategies"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved strategy summary")
    public ResponseEntity<Map<String, Object>> getStrategySummary() {

        log.info("REST request to get strategy summary");

        Map<String, Object> summary = strategyService.getStrategySummary();

        log.info("Retrieved strategy summary with {} metrics", summary.size());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/multi-language-search")
    @Operation(
            summary = "Search by multi-language names",
            description = "Search strategies by primary and/or secondary language names"
    )
    @ApiResponse(responseCode = "200", description = "Multi-language search completed successfully")
    public ResponseEntity<Page<StrategyResponseDto>> findByMultiLanguageNames(
            @Parameter(description = "Primary language name")
            @RequestParam(required = false) String primaryName,
            @Parameter(description = "Secondary language name")
            @RequestParam(required = false) String secondaryName,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request for multi-language search - primary: '{}', secondary: '{}'",
                primaryName, secondaryName);

        Page<StrategyResponseDto> strategies = strategyService.findByMultiLanguageNames(
                primaryName, secondaryName, pageable);

        log.info("Multi-language search returned {} strategies", strategies.getTotalElements());

        return ResponseEntity.ok(strategies);
    }
}