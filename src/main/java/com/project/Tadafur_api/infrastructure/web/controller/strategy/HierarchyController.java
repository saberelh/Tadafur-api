
// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/HierarchyController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.hierarchy.StrategyHierarchyDto;
import com.project.Tadafur_api.application.service.strategy.HierarchyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/hierarchy")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hierarchy Management", description = "Endpoints for retrieving full strategic hierarchies.")
public class HierarchyController {

    private final HierarchyService hierarchyService;

    /**
     * NEW ENDPOINT
     */
    @GetMapping("/by-owner/{ownerId}")
    @Operation(summary = "Get All Strategy Hierarchies for a Specific Owner",
            description = "Retrieves a list of full hierarchies, one for each strategy owned by the specified authority.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of hierarchies."),
            @ApiResponse(responseCode = "404", description = "Owner (Authority) not found for the given ID.")
    })
    public ResponseEntity<List<StrategyHierarchyDto>> getHierarchiesByOwner(
            @Parameter(description = "The ID of the owner (Authority).", example = "2172")
            @PathVariable Long ownerId,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for all hierarchies for owner ID: {}", ownerId);
        return ResponseEntity.ok(hierarchyService.getHierarchiesByOwner(ownerId, lang));
    }

    @GetMapping("/strategy/{strategyId}")
    @Operation(summary = "Get Full Hierarchy for a Single Strategy",
            description = "Retrieves a single strategy and all of its descendants in a nested structure.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hierarchy successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "Strategy not found for the given ID.")
    })
    public ResponseEntity<StrategyHierarchyDto> getFullHierarchy(
            @Parameter(description = "Unique identifier of the root strategy.", example = "1")
            @PathVariable Long strategyId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for full hierarchy of strategy ID: {}", strategyId);
        return ResponseEntity.ok(hierarchyService.getFullHierarchy(strategyId, lang));
    }
    @GetMapping("/v2/hierarchy")
    @Operation(summary = "Get Full Strategy Hierarchy (High-Performance)",
            description = "Retrieves strategies and their descendants using a fast, single-query approach.")
    public ResponseEntity<List<StrategyHierarchyDto>> getFastFullHierarchy(
            @Parameter(description = "Optional: The ID of the root strategy to fetch.")
            @RequestParam(required = false) Long strategyId,
            @Parameter(description = "Optional: The ID of an owner to filter projects by.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for FAST full hierarchy with strategyId: {} and ownerId: {}", strategyId, ownerId);
        return ResponseEntity.ok(hierarchyService.getFastFullHierarchy(strategyId, ownerId, lang));
    }
}
