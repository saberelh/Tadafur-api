// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/ProgramController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProgramResponseDto;
import com.project.Tadafur_api.application.service.strategy.ProgramService;
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
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Program Management", description = "Endpoints for managing programs.")
public class ProgramController {

    private final ProgramService programService;

    /**
     * NEW ENDPOINT: Supports optional filtering by ownerId.
     * - /api/v1/programs -> returns all programs.
     * - /api/v1/programs?ownerId=123 -> returns programs for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Programs with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<ProgramResponseDto>> getPrograms(
            @Parameter(description = "Optional: Filter programs by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for programs with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(programService.getPrograms(Optional.ofNullable(ownerId), lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a Program by ID (Multi-Language)")
    public ResponseEntity<ProgramResponseDto> getProgramById(
            @Parameter(description = "Unique identifier of the program.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for program ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(programService.getById(id, lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/by-goal/{goalId}")
    @Operation(summary = "Get all Programs for a Goal (Multi-Language)")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByGoal(
            @Parameter(description = "The ID of the parent goal.") @PathVariable Long goalId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for programs by goal ID: {}", goalId);
        return ResponseEntity.ok(programService.getByGoalId(goalId, lang));
    }
}