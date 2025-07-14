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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/work-items")
@RequiredArgsConstructor
@Validated
@Tag(name = "Work Item Management", description = "Endpoints for managing project work items.")
public class ProjectWorkItemController {

    private final ProjectWorkItemService workItemService;

    @GetMapping
    @Operation(summary = "Get All Work Items with Optional Assignee Filter (Multi-Language)")
    public ResponseEntity<List<ProjectWorkItemResponseDto>> getWorkItems(
            @Parameter(description = "Optional: Filter work items by the ID of the assignee user.")
            @RequestParam(required = false) Integer assigneeUserId,
            @Parameter(description = "Language code for translation.")
            @RequestParam(defaultValue = "en") String lang) {

        log.info("Received GET request for work items with assigneeUserId: {}", Optional.ofNullable(assigneeUserId).map(String::valueOf).orElse("ALL"));
        // This line will now compile correctly
        return ResponseEntity.ok(workItemService.getWorkItems(Optional.ofNullable(assigneeUserId), lang));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Work Item by ID (Multi-Language)")
    public ResponseEntity<ProjectWorkItemResponseDto> getWorkItemById(
            @Parameter(description = "Unique identifier of the work item.") @PathVariable Long id,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for work item ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(workItemService.getById(id, lang));
    }

    @GetMapping("/by-project/{projectId}")
    @Operation(summary = "Get all Work Items for a Project (Multi-Language)")
    public ResponseEntity<List<ProjectWorkItemResponseDto>> getWorkItemsByProject(
            @Parameter(description = "The ID of the parent project.") @PathVariable Long projectId,
            @Parameter(description = "Language code for translation.") @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for work items by project ID: {}", projectId);
        return ResponseEntity.ok(workItemService.getByProjectId(projectId, lang));
    }
}
