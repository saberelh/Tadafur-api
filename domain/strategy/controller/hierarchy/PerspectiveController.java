package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.PerspectiveResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.PerspectiveService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategy/hierarchy/perspectives")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Perspective Management", description = "Strategic Planning Hierarchy - Perspective Level Operations")
public class PerspectiveController {

    private final PerspectiveService perspectiveService;

    @GetMapping
    @Operation(
            summary = "Get all active perspectives",
            description = "Retrieves a paginated list of all active perspectives with budget information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspectives")
    public ResponseEntity<Page<PerspectiveResponseDto>> getAllPerspectives(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all perspectives - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<PerspectiveResponseDto> perspectives = perspectiveService.getAllActivePerspectives(pageable);

        log.info("Retrieved {} perspectives out of {} total",
                perspectives.getNumberOfElements(), perspectives.getTotalElements());

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get perspective by ID",
            description = "Retrieves detailed information about a specific perspective"
    )
    @ApiResponse(responseCode = "200", description = "Perspective found and returned")
    @ApiResponse(responseCode = "404", description = "Perspective not found")
    public ResponseEntity<PerspectiveResponseDto> getPerspectiveById(
            @Parameter(description = "Perspective ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get perspective by ID: {}", id);

        PerspectiveResponseDto perspective = perspectiveService.getPerspectiveById(id);

        log.info("Successfully retrieved perspective: {} ({})",
                perspective.getDisplayName(), perspective.getId());

        return ResponseEntity.ok(perspective);
    }

    @GetMapping("/strategy/{strategyId}")
    @Operation(
            summary = "Get perspectives by strategy",
            description = "Retrieves all perspectives belonging to a specific strategy"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspectives for strategy")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectivesByStrategy(
            @Parameter(description = "Strategy ID", required = true)
            @PathVariable @NotNull @Min(1) Long strategyId) {

        log.info("REST request to get perspectives by strategy: {}", strategyId);

        List<PerspectiveResponseDto> perspectives = perspectiveService.getPerspectivesByStrategy(strategyId);

        log.info("Retrieved {} perspectives for strategy: {}", perspectives.size(), strategyId);

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get perspectives by owner",
            description = "Retrieves all perspectives owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspectives for owner")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectivesByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get perspectives by owner: {}", ownerId);

        List<PerspectiveResponseDto> perspectives = perspectiveService.getPerspectivesByOwner(ownerId);

        log.info("Retrieved {} perspectives for owner: {}", perspectives.size(), ownerId);

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search perspectives",
            description = "Search perspectives by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<PerspectiveResponseDto>> searchPerspectives(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search perspectives with query: '{}'", query);

        Page<PerspectiveResponseDto> perspectives = perspectiveService.searchPerspectives(query, pageable);

        log.info("Search returned {} perspectives for query: '{}'",
                perspectives.getTotalElements(), query);

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/planning-status/{planningStatus}")
    @Operation(
            summary = "Get perspectives by planning status",
            description = "Retrieves perspectives with a specific planning status"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspectives by planning status")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectivesByPlanningStatus(
            @Parameter(description = "Planning status", required = true)
            @PathVariable @NotNull String planningStatus) {

        log.info("REST request to get perspectives by planning status: {}", planningStatus);

        List<PerspectiveResponseDto> perspectives = perspectiveService.getPerspectivesByPlanningStatus(planningStatus);

        log.info("Retrieved {} perspectives for planning status: {}", perspectives.size(), planningStatus);

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/budget-range")
    @Operation(
            summary = "Get perspectives by budget range",
            description = "Retrieves perspectives within the specified budget range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspectives in budget range")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectivesByBudgetRange(
            @Parameter(description = "Minimum budget", required = true)
            @RequestParam @NotNull BigDecimal minBudget,
            @Parameter(description = "Maximum budget", required = true)
            @RequestParam @NotNull BigDecimal maxBudget) {

        log.info("REST request to get perspectives by budget range: {} to {}", minBudget, maxBudget);

        List<PerspectiveResponseDto> perspectives = perspectiveService.getPerspectivesByBudgetRange(minBudget, maxBudget);

        log.info("Found {} perspectives in budget range: {} to {}", perspectives.size(), minBudget, maxBudget);

        return ResponseEntity.ok(perspectives);
    }

    @GetMapping("/strategy/{strategyId}/summary")
    @Operation(
            summary = "Get perspective summary by strategy",
            description = "Retrieves aggregated statistics about perspectives within a specific strategy"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspective summary")
    public ResponseEntity<Map<String, Object>> getPerspectiveSummaryByStrategy(
            @Parameter(description = "Strategy ID", required = true)
            @PathVariable @NotNull @Min(1) Long strategyId) {

        log.info("REST request to get perspective summary for strategy: {}", strategyId);

        Map<String, Object> summary = perspectiveService.getPerspectiveSummaryByStrategy(strategyId);

        log.info("Retrieved perspective summary for strategy: {} with {} metrics",
                strategyId, summary.size());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/strategy/{strategyId}/count")
    @Operation(
            summary = "Count perspectives by strategy",
            description = "Returns the count of perspectives within a specific strategy"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspective count")
    public ResponseEntity<Long> countPerspectivesByStrategy(
            @Parameter(description = "Strategy ID", required = true)
            @PathVariable @NotNull @Min(1) Long strategyId) {

        log.info("REST request to count perspectives for strategy: {}", strategyId);

        long count = perspectiveService.countPerspectivesByStrategy(strategyId);

        log.info("Found {} perspectives for strategy: {}", count, strategyId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/owner/{ownerId}/count")
    @Operation(
            summary = "Count perspectives by owner",
            description = "Returns the count of perspectives owned by a specific authority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved perspective count")
    public ResponseEntity<Long> countPerspectivesByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to count perspectives for owner: {}", ownerId);

        long count = perspectiveService.countPerspectivesByOwner(ownerId);

        log.info("Found {} perspectives for owner: {}", count, ownerId);

        return ResponseEntity.ok(count);
    }
}