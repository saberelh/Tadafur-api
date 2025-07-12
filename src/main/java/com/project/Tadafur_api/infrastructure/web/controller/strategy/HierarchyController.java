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

@Slf4j
@RestController
@RequestMapping("/api/v1/hierarchy")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hierarchy Management", description = "Endpoints for retrieving full strategic hierarchies.")
public class HierarchyController {

    private final HierarchyService hierarchyService;

    @GetMapping("/strategy/{strategyId}")
    @Operation(summary = "Get Full Strategy Hierarchy (Multi-Language)",
            description = "Retrieves a strategy and all of its descendants (perspectives, goals, etc.) in a nested structure.")
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
}
