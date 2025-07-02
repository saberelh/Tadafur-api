package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.InitiativeResponseDto;
import com.project.Tadafur_api.domain.strategy.service.hierarchy.InitiativeService;
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
@RequestMapping("/api/strategy/hierarchy/initiatives")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Initiative Management", description = "Strategic Planning Hierarchy - Initiative Level Operations")
public class InitiativeController {

    private final InitiativeService initiativeService;

    @GetMapping
    @Operation(
            summary = "Get all active initiatives",
            description = "Retrieves a paginated list of all active initiatives with progress and budget information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives")
    public ResponseEntity<Page<InitiativeResponseDto>> getAllInitiatives(
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to get all initiatives - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<InitiativeResponseDto> initiatives = initiativeService.getAllActiveInitiatives(pageable);

        log.info("Retrieved {} initiatives out of {} total",
                initiatives.getNumberOfElements(), initiatives.getTotalElements());

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get initiative by ID",
            description = "Retrieves detailed information about a specific initiative"
    )
    @ApiResponse(responseCode = "200", description = "Initiative found and returned")
    @ApiResponse(responseCode = "404", description = "Initiative not found")
    public ResponseEntity<InitiativeResponseDto> getInitiativeById(
            @Parameter(description = "Initiative ID", required = true)
            @PathVariable @NotNull @Min(1) Long id) {

        log.info("REST request to get initiative by ID: {}", id);

        InitiativeResponseDto initiative = initiativeService.getInitiativeById(id);

        log.info("Successfully retrieved initiative: {} ({})",
                initiative.getDisplayName(), initiative.getId());

        return ResponseEntity.ok(initiative);
    }

    @GetMapping("/program/{programId}")
    @Operation(
            summary = "Get initiatives by program",
            description = "Retrieves all initiatives belonging to a specific program"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives for program")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByProgram(
            @Parameter(description = "Program ID", required = true)
            @PathVariable @NotNull @Min(1) Long programId) {

        log.info("REST request to get initiatives by program: {}", programId);

        List<InitiativeResponseDto> initiatives = initiativeService.getInitiativesByProgram(programId);

        log.info("Retrieved {} initiatives for program: {}", initiatives.size(), programId);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(
            summary = "Get initiatives by owner",
            description = "Retrieves all initiatives owned by a specific authority/organization"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives for owner")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByOwner(
            @Parameter(description = "Owner ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerId) {

        log.info("REST request to get initiatives by owner: {}", ownerId);

        List<InitiativeResponseDto> initiatives = initiativeService.getInitiativesByOwner(ownerId);

        log.info("Retrieved {} initiatives for owner: {}", initiatives.size(), ownerId);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search initiatives",
            description = "Search initiatives by name or description in both primary and secondary languages"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<InitiativeResponseDto>> searchInitiatives(
            @Parameter(description = "Search query", required = true)
            @RequestParam @NotNull String query,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("REST request to search initiatives with query: '{}'", query);

        Page<InitiativeResponseDto> initiatives = initiativeService.searchInitiatives(query, pageable);

        log.info("Search returned {} initiatives for query: '{}'",
                initiatives.getTotalElements(), query);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/vision-priority/{visionPriorityId}")
    @Operation(
            summary = "Get initiatives by vision priority",
            description = "Retrieves initiatives aligned with a specific vision priority"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives for vision priority")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByVisionPriority(
            @Parameter(description = "Vision Priority ID", required = true)
            @PathVariable @NotNull String visionPriorityId) {

        log.info("REST request to get initiatives by vision priority: {}", visionPriorityId);

        List<InitiativeResponseDto> initiatives = initiativeService.getInitiativesByVisionPriority(visionPriorityId);

        log.info("Retrieved {} initiatives for vision priority: {}", initiatives.size(), visionPriorityId);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/owner-node/{ownerNodeId}")
    @Operation(
            summary = "Get initiatives by owner organizational node",
            description = "Retrieves initiatives owned by a specific organizational chart node"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives for owner node")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByOwnerNode(
            @Parameter(description = "Owner Node ID", required = true)
            @PathVariable @NotNull @Min(1) Long ownerNodeId) {

        log.info("REST request to get initiatives by owner node: {}", ownerNodeId);

        List<InitiativeResponseDto> initiatives = initiativeService.getInitiativesByOwnerNode(ownerNodeId);

        log.info("Retrieved {} initiatives for owner node: {}", initiatives.size(), ownerNodeId);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/active-on-date")
    @Operation(
            summary = "Get initiatives active on specific date",
            description = "Retrieves initiatives that are active (within timeline) on the specified date"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active initiatives")
    public ResponseEntity<List<InitiativeResponseDto>> getActiveInitiativesOnDate(
            @Parameter(description = "Date to check (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get initiatives active on date: {}", date);

        List<InitiativeResponseDto> initiatives = initiativeService.getActiveInitiativesOnDate(date);

        log.info("Found {} initiatives active on date: {}", initiatives.size(), date);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/timeline-range")
    @Operation(
            summary = "Get initiatives by timeline range",
            description = "Retrieves initiatives that start within the specified date range"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives in timeline range")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByTimelineRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        log.info("REST request to get initiatives by timeline range: {} to {}", fromDate, toDate);

        List<InitiativeResponseDto> initiatives = initiativeService.getInitiativesByTimelineRange(fromDate, toDate);

        log.info("Found {} initiatives in timeline range: {} to {}", initiatives.size(), fromDate, toDate);

        return ResponseEntity.ok(initiatives);
    }

    @GetMapping("/minimum-progress/{minProgress}")
    @Operation(
            summary = "Get initiatives by minimum progress",
            description = "Retrieves initiatives that have achieved at least the specified progress percentage"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved initiatives with minimum progress")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByMinimumProgress(
            @Parameter(description = "Minimum progress percentage (0-100)", required = true)
            @PathVariable @NotNull @Min(0) BigDecimal minProgress) {