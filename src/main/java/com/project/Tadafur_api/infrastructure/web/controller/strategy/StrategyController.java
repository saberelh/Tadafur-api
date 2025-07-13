// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/StrategyController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.service.strategy.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/strategies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Strategy Management", description = "Endpoints for managing strategies.")
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * REFACTORED ENDPOINT: Now supports optional filtering by ownerId.
     * - /api/v1/strategies -> returns all strategies.
     * - /api/v1/strategies?ownerId=123 -> returns strategies for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Strategies with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<StrategyResponseDto>> getStrategies(
            @Parameter(description = "Optional: Filter strategies by the ID of the owner (Authority). If omitted, all strategies are returned.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for strategies with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(strategyService.getStrategies(Optional.ofNullable(ownerId), lang));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Strategy by ID (Multi-Language)")
    public ResponseEntity<StrategyResponseDto> getStrategyById(
            @Parameter(description = "Unique identifier of the strategy.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for strategy ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(strategyService.getById(id, lang));
    }
}