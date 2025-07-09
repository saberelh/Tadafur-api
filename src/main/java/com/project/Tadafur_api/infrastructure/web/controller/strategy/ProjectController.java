// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/ProjectController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectResponseDto;
import com.project.Tadafur_api.application.service.strategy.ProjectService;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Validated
@Tag(name = "Project Management", description = "Endpoints for managing projects.")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a Project by ID (Multi-Language)")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @Parameter(description = "Unique identifier of the project.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for project ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(projectService.getById(id, lang));
    }

    @GetMapping("/by-initiative/{initiativeId}")
    @Operation(summary = "Get all Projects for an Initiative (Multi-Language)")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByInitiative(
            @Parameter(description = "The ID of the parent initiative.", example = "1")
            @PathVariable Long initiativeId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for projects by initiative ID: {}", initiativeId);
        return ResponseEntity.ok(projectService.getByInitiativeId(initiativeId, lang));
    }
}