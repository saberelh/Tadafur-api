package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.GoalResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.GoalService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategy/hierarchy/goals")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Goal Management", description = "Strategic Planning Hierarchy - Goal Level Operations")
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    @Operation(
            summary = "Get all active goals",
            description = "Retrieves a paginated list of all active goals with progress and budget information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goals")
    public ResponseEntity<Page<GoalResponseDto>> getAllGoals(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all goals - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<GoalResponseDto> goals = goalService.getAllActiveGoals(pageable);

        log.info("Retrieved {} goals out of {} total",
                goals.getNumberOfElements(), goals.getTotalElements());

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get goal by ID",
            description = "Retrieves detailed information about a specific goal including progress and vision alignment"
    )
    @ApiResponse(responseCode = "200", description = "Goal found and returned")
    @ApiResponse(responseCode = "404", description = "Goal not found")
    public ResponseEntity<GoalResponseDto> getGoalById(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get goal by ID: {}", id);

        GoalResponseDto goal = goalService.getGoalById(id);

        log.info("Successfully retrieved goal: {} ({})",
                goal.getDisplayName(), goal.getId());

        return ResponseEntity.ok(goal);
    }

    @GetMapping("/perspective/{perspectiveId}")
    @Operation(
            summary = "Get goals by perspective",
            description = "Retrieves all goals belonging to a specific perspective"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goals for perspective")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByPerspective(
            @Parameter(description = "Perspective ID", required = true)
            @PathVariable @NotNull @Min(1) Long perspectiveId) {

        log.info("REST request to get goals by perspective: {}", perspectiveId);

        List<GoalResponseDto> goals = goalService.getGoalsByPerspective(perspectiveId);

        log.info("Retrieved {} goals for perspective: {}", goals.size(), perspectiveId);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get goals by owner",
            description = "Retrieves all goals owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goals for owner")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get goals by owner: {}", ownerId);

        List<GoalResponseDto> goals = goalService.getGoalsByOwner(ownerId);

        log.info("Retrieved {} goals for owner: {}", goals.size(), ownerId);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search goals",
            description = "Search goals by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<GoalResponseDto>> searchGoals(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search goals with query: '{}'", query);

        Page<GoalResponseDto> goals = goalService.searchGoals(query, pageable);

        log.info("Search returned {} goals for query: '{}'",
                goals.getTotalElements(), query);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/active-on-date")
    @Operation(
            summary = "Get goals active on specific date",
            description = "Retrieves goals that are active (within timeline) on the specified date"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active goals")
    public ResponseEntity<List<GoalResponseDto>> getActiveGoalsOnDate(
            @Parameter(description = "Date to check (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get goals active on date: {}", date);

        List<GoalResponseDto> goals = goalService.getActiveGoalsOnDate(date);

        log.info("Found {} goals active on date: {}", goals.size(), date);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/vision-priority/{visionPriority}")
    @Operation(
            summary = "Get goals by vision priority",
            description = "Retrieves goals aligned with a specific vision priority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goals for vision priority")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByVisionPriority(
            @Parameter(description = "Vision Priority ID", required = true)
            @PathVariable @NotNull @Min(1) Long visionPriority) {

        log.info("REST request to get goals by vision priority: {}", visionPriority);

        List<GoalResponseDto> goals = goalService.getGoalsByVisionPriority(visionPriority);

        log.info("Retrieved {} goals for vision priority: {}", goals.size(), visionPriority);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/minimum-progress/{minProgress}")
    @Operation(
            summary = "Get goals by minimum progress",
            description = "Retrieves goals that have achieved at least the specified progress percentage"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goals with minimum progress")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByMinimumProgress(
            @Parameter(description = "Minimum progress percentage (0-100)", required = true)
            @PathVariable @NotNull @Min(0) BigDecimal minProgress) {

        log.info("REST request to get goals with minimum progress: {}", minProgress);

        List<GoalResponseDto> goals = goalService.getGoalsByMinimumProgress(minProgress);

        log.info("Retrieved {} goals with minimum progress: {}", goals.size(), minProgress);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/perspective/{perspectiveId}/summary")
    @Operation(
            summary = "Get goal summary by perspective",
            description = "Retrieves aggregated statistics about goals within a specific perspective"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved goal summary")
    public ResponseEntity<Map<String, Object>> getGoalSummaryByPerspective(
            @Parameter(description = "Perspective ID", required = true)
            @PathVariable @NotNull @Min(1) Long perspectiveId) {

        log.info("REST request to get goal summary for perspective: {}", perspectiveId);

        Map<String, Object> summary = goalService.getGoalSummaryByPerspective(perspectiveId);

        log.info("Retrieved goal summary for perspective: {} with {} metrics",
                perspectiveId, summary.size());

        return ResponseEntity.ok(summary);
    }
}