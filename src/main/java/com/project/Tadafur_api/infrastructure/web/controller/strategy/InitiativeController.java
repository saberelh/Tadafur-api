// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/InitiativeController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.InitiativeResponseDto;
import com.project.Tadafur_api.application.service.strategy.InitiativeService;
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
@RequestMapping("/api/v1/initiatives")
@RequiredArgsConstructor
@Validated
@Tag(name = "Initiative Management", description = "Endpoints for managing initiatives.")
public class InitiativeController {

    private final InitiativeService initiativeService;

    /**
     * NEW ENDPOINT: Supports optional filtering by ownerId.
     * - /api/v1/initiatives -> returns all initiatives.
     * - /api/v1/initiatives?ownerId=123 -> returns initiatives for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Initiatives with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiatives(
            @Parameter(description = "Optional: Filter initiatives by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for initiatives with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(initiativeService.getInitiatives(Optional.ofNullable(ownerId), lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an Initiative by ID (Multi-Language)")
    public ResponseEntity<InitiativeResponseDto> getInitiativeById(
            @Parameter(description = "Unique identifier of the initiative.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for initiative ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(initiativeService.getById(id, lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/by-program/{programId}")
    @Operation(summary = "Get all Initiatives for a Program (Multi-Language)")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByProgram(
            @Parameter(description = "The ID of the parent program.") @PathVariable Long programId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for initiatives by program ID: {}", programId);
        return ResponseEntity.ok(initiativeService.getByProgramId(programId, lang));
    }
}