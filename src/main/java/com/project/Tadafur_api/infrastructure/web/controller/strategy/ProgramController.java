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

@Slf4j
@RestController
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Program Management", description = "Endpoints for managing programs.")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a Program by ID (Multi-Language)")
    public ResponseEntity<ProgramResponseDto> getProgramById(
            @Parameter(description = "Unique identifier of the program.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for program ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(programService.getById(id, lang));
    }

    @GetMapping("/by-goal/{goalId}")
    @Operation(summary = "Get all Programs for a Goal (Multi-Language)")
    public ResponseEntity<List<ProgramResponseDto>> getProgramsByGoal(
            @Parameter(description = "The ID of the parent goal.", example = "1")
            @PathVariable Long goalId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for programs by goal ID: {}", goalId);
        return ResponseEntity.ok(programService.getByGoalId(goalId, lang));
    }
}