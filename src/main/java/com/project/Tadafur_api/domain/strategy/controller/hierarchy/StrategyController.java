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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
            description = "Retrieves a paginated list of all active strategies"
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
            description = "Retrieves detailed information about a specific strategy"
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
            description = "Retrieves all strategies owned by a specific authority"
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
            description = "Search strategies by name or description"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<StrategyResponseDto>> searchStrategies(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search strategies with query: '{}'", query);

        Page<StrategyResponseDto> strategies = strategyService.searchStrategies(query, pageable);

        log.info("Search returned {} strategies for query: '{}'",
                strategies.getTotalElements(), query);

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
}