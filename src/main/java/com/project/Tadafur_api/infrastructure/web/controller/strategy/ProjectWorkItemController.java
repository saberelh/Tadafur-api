// File: src/main/java/com/project/Tadafur_api/infrastructure/web/controller/strategy/ProjectWorkItemController.java
package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectWorkItemResponseDto;
import com.project.Tadafur_api.application.service.strategy.ProjectWorkItemService;
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
@RequestMapping("/api/v1/work-items")
@RequiredArgsConstructor
@Validated
@Tag(name = "Work Item Management", description = "Endpoints for managing project work items.")
public class ProjectWorkItemController {

    private final ProjectWorkItemService workItemService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a Work Item by ID (Multi-Language)")
    public ResponseEntity<ProjectWorkItemResponseDto> getWorkItemById(
            @Parameter(description = "Unique identifier of the work item.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for work item ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(workItemService.getById(id, lang));
    }

    @GetMapping("/by-project/{projectId}")
    @Operation(summary = "Get all Work Items for a Project (Multi-Language)")
    public ResponseEntity<List<ProjectWorkItemResponseDto>> getWorkItemsByProject(
            @Parameter(description = "The ID of the parent project.", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Language code for translation.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for work items by project ID: {}", projectId);
        return ResponseEntity.ok(workItemService.getByProjectId(projectId, lang));
    }
}