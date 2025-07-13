// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/PerspectiveController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.PerspectiveResponseDto;
import com.project.Tadafur_api.application.service.strategy.PerspectiveService;
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
@RequestMapping("/api/v1/perspectives")
@RequiredArgsConstructor
@Validated
@Tag(name = "Perspective Management", description = "Endpoints for managing perspectives.")
public class PerspectiveController {

    private final PerspectiveService perspectiveService;

    /**
     * NEW ENDPOINT: Supports optional filtering by ownerId.
     * - /api/v1/perspectives -> returns all perspectives.
     * - /api/v1/perspectives?ownerId=123 -> returns perspectives for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Perspectives with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectives(
            @Parameter(description = "Optional: Filter perspectives by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for perspectives with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(perspectiveService.getPerspectives(Optional.ofNullable(ownerId), lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a Perspective by ID (Multi-Language)")
    public ResponseEntity<PerspectiveResponseDto> getPerspectiveById(
            @Parameter(description = "Unique identifier of the perspective.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for perspective ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(perspectiveService.getById(id, lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/by-strategy/{strategyId}")
    @Operation(summary = "Get all Perspectives for a Strategy (Multi-Language)")
    public ResponseEntity<List<PerspectiveResponseDto>> getPerspectivesByStrategy(
            @Parameter(description = "The ID of the parent strategy.") @PathVariable Long strategyId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for perspectives by strategy ID: {}", strategyId);
        return ResponseEntity.ok(perspectiveService.getByStrategyId(strategyId, lang));
    }
}