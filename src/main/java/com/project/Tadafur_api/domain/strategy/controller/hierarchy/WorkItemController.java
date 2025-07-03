package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.ProjectResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.ProjectService;
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
@RequestMapping("/api/strategy/hierarchy/projects")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Project Management", description = "Strategic Planning Hierarchy - Project Level Operations")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(
            summary = "Get all active projects",
            description = "Retrieves a paginated list of all active projects with progress and budget information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects")
    public ResponseEntity<Page<ProjectResponseDto>> getAllProjects(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all projects - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<ProjectResponseDto> projects = projectService.getAllActiveProjects(pageable);

        log.info("Retrieved {} projects out of {} total",
                projects.getNumberOfElements(), projects.getTotalElements());

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get project by ID",
            description = "Retrieves detailed information about a specific project including progress and timeline"
    )
    @ApiResponse(responseCode = "200", description = "Project found and returned")
    @ApiResponse(responseCode = "404", description = "Project not found")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @Parameter(description = "Project ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get project by ID: {}", id);

        ProjectResponseDto project = projectService.getProjectById(id);

        log.info("Successfully retrieved project: {} ({})",
                project.getDisplayName(), project.getId());

        return ResponseEntity.ok(project);
    }

    @GetMapping("/initiative/{initiativeId}")
    @Operation(
            summary = "Get projects by initiative",
            description = "Retrieves all projects belonging to a specific initiative"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects for initiative")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByInitiative(
            @Parameter(description = "Initiative ID", required = true)
            @PathVariable @NotNull @Min(1) Long initiativeId) {

        log.info("REST request to get projects by initiative: {}", initiativeId);

        List<ProjectResponseDto> projects = projectService.getProjectsByInitiative(initiativeId);

        log.info("Retrieved {} projects for initiative: {}", projects.size(), initiativeId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get projects by owner",
            description = "Retrieves all projects owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects for owner")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get projects by owner: {}", ownerId);

        List<ProjectResponseDto> projects = projectService.getProjectsByOwner(ownerId);

        log.info("Retrieved {} projects for owner: {}", projects.size(), ownerId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search projects",
            description = "Search projects by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<ProjectResponseDto>> searchProjects(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search projects with query: '{}'", query);

        Page<ProjectResponseDto> projects = projectService.searchProjects(query, pageable);

        log.info("Search returned {} projects for query: '{}'",
                projects.getTotalElements(), query);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active-on-date")
    @Operation(
            summary = "Get projects active on specific date",
            description = "Retrieves projects that are active (within timeline) on the specified date"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active projects")
    public ResponseEntity<List<ProjectResponseDto>> getActiveProjectsOnDate(
            @Parameter(description = "Date to check (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get projects active on date: {}", date);

        List<ProjectResponseDto> projects = projectService.getActiveProjectsOnDate(date);

        log.info("Found {} projects active on date: {}", projects.size(), date);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/timeline-range")
    @Operation(
            summary = "Get projects by timeline range",
            description = "Retrieves projects that start within the specified date range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects in timeline range")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByTimelineRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        log.info("REST request to get projects by timeline range: {} to {}", fromDate, toDate);

        List<ProjectResponseDto> projects = projectService.getProjectsByTimelineRange(fromDate, toDate);

        log.info("Found {} projects in timeline range: {} to {}", projects.size(), fromDate, toDate);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/minimum-progress/{minProgress}")
    @Operation(
            summary = "Get projects by minimum progress",
            description = "Retrieves projects that have achieved at least the specified progress percentage"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects with minimum progress")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByMinimumProgress(
            @Parameter(description = "Minimum progress percentage (0-100)", required = true)
            @PathVariable @NotNull @Min(0) BigDecimal minProgress) {

        log.info("REST request to get projects with minimum progress: {}", minProgress);

        List<ProjectResponseDto> projects = projectService.getProjectsByMinimumProgress(minProgress);

        log.info("Retrieved {} projects with minimum progress: {}", projects.size(), minProgress);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/priority/{priorityId}")
    @Operation(
            summary = "Get projects by priority",
            description = "Retrieves projects with a specific priority level"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects by priority")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByPriority(
            @Parameter(description = "Priority ID", required = true)
            @PathVariable @NotNull @Min(1) Long priorityId) {

        log.info("REST request to get projects by priority: {}", priorityId);

        List<ProjectResponseDto> projects = projectService.getProjectsByPriority(priorityId);

        log.info("Retrieved {} projects for priority: {}", projects.size(), priorityId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/status/{statusId}")
    @Operation(
            summary = "Get projects by status",
            description = "Retrieves projects with a specific status"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects by status")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByStatus(
            @Parameter(description = "Status ID", required = true)
            @PathVariable @NotNull @Min(1) Long statusId) {

        log.info("REST request to get projects by status: {}", statusId);

        List<ProjectResponseDto> projects = projectService.getProjectsByStatus(statusId);

        log.info("Retrieved {} projects for status: {}", projects.size(), statusId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/methodology/{methodologyId}")
    @Operation(
            summary = "Get projects by methodology",
            description = "Retrieves projects using a specific methodology"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects by methodology")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByMethodology(
            @Parameter(description = "Methodology ID", required = true)
            @PathVariable @NotNull @Min(1) Long methodologyId) {

        log.info("REST request to get projects by methodology: {}", methodologyId);

        List<ProjectResponseDto> projects = projectService.getProjectsByMethodology(methodologyId);

        log.info("Retrieved {} projects for methodology: {}", projects.size(), methodologyId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/vision-priority/{visionPriorityId}")
    @Operation(
            summary = "Get projects by vision priority",
            description = "Retrieves projects aligned with a specific vision priority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects for vision priority")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByVisionPriority(
            @Parameter(description = "Vision Priority ID", required = true)
            @PathVariable @NotNull String visionPriorityId) {

        log.info("REST request to get projects by vision priority: {}", visionPriorityId);

        List<ProjectResponseDto> projects = projectService.getProjectsByVisionPriority(visionPriorityId);

        log.info("Retrieved {} projects for vision priority: {}", projects.size(), visionPriorityId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/budget-range")
    @Operation(
            summary = "Get projects by budget range",
            description = "Retrieves projects within the specified budget range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects in budget range")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByBudgetRange(
            @Parameter(description = "Minimum budget", required = true)
            @RequestParam @NotNull BigDecimal minBudget,
            @Parameter(description = "Maximum budget", required = true)
            @RequestParam @NotNull BigDecimal maxBudget) {

        log.info("REST request to get projects by budget range: {} to {}", minBudget, maxBudget);

        List<ProjectResponseDto> projects = projectService.getProjectsByBudgetRange(minBudget, maxBudget);

        log.info("Found {} projects in budget range: {} to {}", projects.size(), minBudget, maxBudget);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/overdue")
    @Operation(
            summary = "Get overdue projects",
            description = "Retrieves projects that are overdue (past end date with incomplete progress)"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved overdue projects")
    public ResponseEntity<List<ProjectResponseDto>> getOverdueProjects() {

        log.info("REST request to get overdue projects");

        List<ProjectResponseDto> projects = projectService.getOverdueProjects();

        log.info("Found {} overdue projects", projects.size());

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/type/{type}")
    @Operation(
            summary = "Get projects by type",
            description = "Retrieves projects of a specific type"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved projects by type")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByType(
            @Parameter(description = "Project type", required = true)
            @PathVariable @NotNull Integer type) {

        log.info("REST request to get projects by type: {}", type);

        List<ProjectResponseDto> projects = projectService.getProjectsByType(type);

        log.info("Retrieved {} projects for type: {}", projects.size(), type);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/initiative/{initiativeId}/summary")
    @Operation(
            summary = "Get project summary by initiative",
            description = "Retrieves aggregated statistics about projects within a specific initiative"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved project summary")
    public ResponseEntity<Map<String, Object>> getProjectSummaryByInitiative(
            @Parameter(description = "Initiative ID", required = true)
            @PathVariable @NotNull @Min(1) Long initiativeId) {

        log.info("REST request to get project summary for initiative: {}", initiativeId);

        Map<String, Object> summary = projectService.getProjectSummaryByInitiative(initiativeId);

        log.info("Retrieved project summary for initiative: {} with {} metrics",
                initiativeId, summary.size());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/initiative/{initiativeId}/count")
    @Operation(
            summary = "Count projects by initiative",
            description = "Returns the count of projects within a specific initiative"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved project count")
    public ResponseEntity<Long> countProjectsByInitiative(
            @Parameter(description = "Initiative ID", required = true)
            @PathVariable @NotNull @Min(1) Long initiativeId) {

        log.info("REST request to count projects for initiative: {}", initiativeId);

        long count = projectService.countProjectsByInitiative(initiativeId);

        log.info("Found {} projects for initiative: {}", count, initiativeId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/owner/{ownerId}/count")
    @Operation(
            summary = "Count projects by owner",
            description = "Returns the count of projects owned by a specific authority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved project count")
    public ResponseEntity<Long> countProjectsByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to count projects for owner: {}", ownerId);

        long count = projectService.countProjectsByOwner(ownerId);

        log.info("Found {} projects for owner: {}", count, ownerId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/priority/{priorityId}/count")
    @Operation(
            summary = "Count projects by priority",
            description = "Returns the count of projects with a specific priority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved project count")
    public ResponseEntity<Long> countProjectsByPriority(
            @Parameter(description = "Priority ID", required = true)
            @PathVariable @NotNull @Min(1) Long priorityId) {

        log.info("REST request to count projects for priority: {}", priorityId);

        long count = projectService.countProjectsByPriority(priorityId);

        log.info("Found {} projects for priority: {}", count, priorityId);

        return ResponseEntity.ok(count);
    }
}