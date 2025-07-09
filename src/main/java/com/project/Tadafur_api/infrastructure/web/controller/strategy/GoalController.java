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

@Slf4j
@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Validated
@Tag(name = "Goal Management", description = "Endpoints for managing goals.")
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a Goal by ID (Multi-Language)")
    public ResponseEntity<GoalResponseDto> getGoalById(
            @Parameter(description = "Unique identifier of the goal.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for goal ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(goalService.getById(id, lang));
    }

    @GetMapping("/by-perspective/{perspectiveId}")
    @Operation(summary = "Get all Goals for a Perspective (Multi-Language)")
    public ResponseEntity<List<GoalResponseDto>> getGoalsByPerspective(
            @Parameter(description = "The ID of the parent perspective.", example = "1")
            @PathVariable Long perspectiveId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for goals by perspective ID: {}", perspectiveId);
        return ResponseEntity.ok(goalService.getByPerspectiveId(perspectiveId, lang));
    }
}