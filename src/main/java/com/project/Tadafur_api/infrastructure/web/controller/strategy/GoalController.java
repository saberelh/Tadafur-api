// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/GoalController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.GoalResponseDto;
import com.project.Tadafur_api.application.service.strategy.GoalService;
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
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Validated
@Tag(name = "Goal Management", description = "Endpoints for managing goals.")
public class GoalController {

    private final GoalService goalService;

    /**
     * NEW ENDPOINT: Supports optional filtering by ownerId.
     * - /api/v1/goals -> returns all goals.
     * - /api/v1/goals?ownerId=123 -> returns goals for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Goals with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<GoalResponseDto>> getGoals(
            @Parameter(description = "Optional: Filter goals by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for goals with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(goalService.getGoals(Optional.ofNullable(ownerId), lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a Goal by ID (Multi-Language)")
    public ResponseEntity<GoalResponseDto> getGoalById(
            @Parameter(description = "Unique identifier of the goal.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for goal ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(goalService.getById(id, lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/by-perspective/{perspectiveId}")
    @Operation(summary = "Get all Goals for a Perspective (Multi-Language)")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByPerspective(
            @Parameter(description = "The ID of the parent perspective.") @PathVariable Long perspectiveId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for goals by perspective ID: {}", perspectiveId);
        return ResponseEntity.ok(goalService.getByPerspectiveId(perspectiveId, lang));
    }
}