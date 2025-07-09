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

@Slf4j
@RestController
@RequestMapping("/api/v1/initiatives")
@RequiredArgsConstructor
@Validated
@Tag(name = "Initiative Management", description = "Endpoints for managing initiatives.")
public class InitiativeController {

    private final InitiativeService initiativeService;

    @GetMapping("/{id}")
    @Operation(summary = "Get an Initiative by ID (Multi-Language)")
    public ResponseEntity<InitiativeResponseDto> getInitiativeById(
            @Parameter(description = "Unique identifier of the initiative.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for initiative ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(initiativeService.getById(id, lang));
    }

    @GetMapping("/by-program/{programId}")
    @Operation(summary = "Get all Initiatives for a Program (Multi-Language)")
    public ResponseEntity<List<InitiativeResponseDto>> getInitiativesByProgram(
            @Parameter(description = "The ID of the parent program.", example = "1")
            @PathVariable Long programId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for initiatives by program ID: {}", programId);
        return ResponseEntity.ok(initiativeService.getByProgramId(programId, lang));
    }
}