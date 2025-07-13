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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Validated
@Tag(name = "Project Management", description = "Endpoints for managing projects.")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * NEW ENDPOINT: Supports optional filtering by ownerId.
     * - /api/v1/projects -> returns all projects.
     * - /api/v1/projects?ownerId=123 -> returns projects for owner 123.
     */
    @GetMapping
    @Operation(summary = "Get All Projects with Optional Owner Filter (Multi-Language)")
    public ResponseEntity<List<ProjectResponseDto>> getProjects(
            @Parameter(description = "Optional: Filter projects by the ID of the owner.")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for projects with ownerId: {}", Optional.ofNullable(ownerId).map(String::valueOf).orElse("ALL"));
        return ResponseEntity.ok(projectService.getProjects(Optional.ofNullable(ownerId), lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a Project by ID (Multi-Language)")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @Parameter(description = "Unique identifier of the project.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for project ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(projectService.getById(id, lang));
    }

    /**
     * UNCHANGED ENDPOINT
     */
    @GetMapping("/by-initiative/{initiativeId}")
    @Operation(summary = "Get all Projects for an Initiative (Multi-Language)")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByInitiative(
            @Parameter(description = "The ID of the parent initiative.") @PathVariable Long initiativeId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for projects by initiative ID: {}", initiativeId);
        return ResponseEntity.ok(projectService.getByInitiativeId(initiativeId, lang));
    }
}
