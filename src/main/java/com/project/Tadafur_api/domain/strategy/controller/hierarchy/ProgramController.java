package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.ProgramResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.ProgramService;
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
@RequestMapping("/api/strategy/hierarchy/programs")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Program Management", description = "Strategic Planning Hierarchy - Program Level Operations")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping
    @Operation(
            summary = "Get all active programs",
            description = "Retrieves a paginated list of all active programs with progress and budget information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs")
    public ResponseEntity<Page<ProgramResponseDto>> getAllPrograms(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all programs - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<ProgramResponseDto> programs = programService.getAllActivePrograms(pageable);

        log.info("Retrieved {} programs out of {} total",
                programs.getNumberOfElements(), programs.getTotalElements());

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get program by ID",
            description = "Retrieves detailed information about a specific program"
    )
    @ApiResponse(responseCode = "200", description = "Program found and returned")
    @ApiResponse(responseCode = "404", description = "Program not found")
    public ResponseEntity<ProgramResponseDto> getProgramById(
            @Parameter(description = "Program ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get program by ID: {}", id);

        ProgramResponseDto program = programService.getProgramById(id);

        log.info("Successfully retrieved program: {} ({})",
                program.getDisplayName(), program.getId());

        return ResponseEntity.ok(program);
    }

    @GetMapping("/goal/{goalId}")
    @Operation(
            summary = "Get programs by goal",
            description = "Retrieves all programs belonging to a specific goal"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs for goal")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable @NotNull @Min(1) Long goalId) {

        log.info("REST request to get programs by goal: {}", goalId);

        List<ProgramResponseDto> programs = programService.getProgramsByGoal(goalId);

        log.info("Retrieved {} programs for goal: {}", programs.size(), goalId);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get programs by owner",
            description = "Retrieves all programs owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs for owner")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get programs by owner: {}", ownerId);

        List<ProgramResponseDto> programs = programService.getProgramsByOwner(ownerId);

        log.info("Retrieved {} programs for owner: {}", programs.size(), ownerId);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search programs",
            description = "Search programs by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<ProgramResponseDto>> searchPrograms(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search programs with query: '{}'", query);

        Page<ProgramResponseDto> programs = programService.searchPrograms(query, pageable);

        log.info("Search returned {} programs for query: '{}'",
                programs.getTotalElements(), query);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/minimum-progress/{minProgress}")
    @Operation(
            summary = "Get programs by minimum progress",
            description = "Retrieves programs that have achieved at least the specified progress percentage"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs with minimum progress")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByMinimumProgress(
            @Parameter(description = "Minimum progress percentage (0-100)", required = true)
            @PathVariable @NotNull @Min(0) BigDecimal minProgress) {

        log.info("REST request to get programs with minimum progress: {}", minProgress);

        List<ProgramResponseDto> programs = programService.getProgramsByMinimumProgress(minProgress);

        log.info("Retrieved {} programs with minimum progress: {}", programs.size(), minProgress);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/vision-priority/{visionPriorityId}")
    @Operation(
            summary = "Get programs by vision priority",
            description = "Retrieves programs aligned with a specific vision priority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs for vision priority")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByVisionPriority(
            @Parameter(description = "Vision Priority ID", required = true)
            @PathVariable @NotNull String visionPriorityId) {

        log.info("REST request to get programs by vision priority: {}", visionPriorityId);

        List<ProgramResponseDto> programs = programService.getProgramsByVisionPriority(visionPriorityId);

        log.info("Retrieved {} programs for vision priority: {}", programs.size(), visionPriorityId);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/minimum-contribution/{minContribution}")
    @Operation(
            summary = "Get programs by minimum contribution",
            description = "Retrieves programs that have at least the specified contribution percentage"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs with minimum contribution")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByMinimumContribution(
            @Parameter(description = "Minimum contribution percentage (0-100)", required = true)
            @PathVariable @NotNull @Min(0) Double minContribution) {

        log.info("REST request to get programs with minimum contribution: {}", minContribution);

        List<ProgramResponseDto> programs = programService.getProgramsByMinimumContribution(minContribution);

        log.info("Retrieved {} programs with minimum contribution: {}", programs.size(), minContribution);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/budget-range")
    @Operation(
            summary = "Get programs by budget range",
            description = "Retrieves programs within the specified budget range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs in budget range")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByBudgetRange(
            @Parameter(description = "Minimum budget", required = true)
            @RequestParam @NotNull BigDecimal minBudget,
            @Parameter(description = "Maximum budget", required = true)
            @RequestParam @NotNull BigDecimal maxBudget) {

        log.info("REST request to get programs by budget range: {} to {}", minBudget, maxBudget);

        List<ProgramResponseDto> programs = programService.getProgramsByBudgetRange(minBudget, maxBudget);

        log.info("Found {} programs in budget range: {} to {}", programs.size(), minBudget, maxBudget);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/planning-status/{planningStatus}")
    @Operation(
            summary = "Get programs by planning status",
            description = "Retrieves programs with a specific planning status"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved programs by planning status")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByPlanningStatus(
            @Parameter(description = "Planning status", required = true)
            @PathVariable @NotNull String planningStatus) {

        log.info("REST request to get programs by planning status: {}", planningStatus);

        List<ProgramResponseDto> programs = programService.getProgramsByPlanningStatus(planningStatus);

        log.info("Retrieved {} programs for planning status: {}", programs.size(), planningStatus);

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/goal/{goalId}/summary")
    @Operation(
            summary = "Get program summary by goal",
            description = "Retrieves aggregated statistics about programs within a specific goal"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved program summary")
    public ResponseEntity<Map<String, Object>> getProgramSummaryByGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable @NotNull @Min(1) Long goalId) {

        log.info("REST request to get program summary for goal: {}", goalId);

        Map<String, Object> summary = programService.getProgramSummaryByGoal(goalId);

        log.info("Retrieved program summary for goal: {} with {} metrics",
                goalId, summary.size());

        return ResponseEntity.ok(summary);
    }
}