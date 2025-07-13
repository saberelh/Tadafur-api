// File: src/main/java/com/project/Tadafur_api/application/service/strategy/ProjectWorkItemService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectWorkItemResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.ProjectWorkItemMapper;
import com.project.Tadafur_api.domain.strategy.entity.ProjectWorkItem;
import com.project.Tadafur_api.domain.strategy.repository.ProjectWorkItemRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectWorkItemService {

    private final ProjectWorkItemRepository workItemRepository;
    private final ProjectWorkItemMapper workItemMapper;

    /**
     * THIS IS THE MISSING METHOD THAT IS NOW ADDED.
     * It gets work items based on an optional assignee user ID.
     */
    public List<ProjectWorkItemResponseDto> getWorkItems(Optional<Integer> assigneeUserId, String lang) {
        List<ProjectWorkItem> workItems;
        if (assigneeUserId.isPresent()) {
            log.info("Fetching work items for assignee user ID: {}", assigneeUserId.get());
            workItems = workItemRepository.findByAssigneeUserId(assigneeUserId.get());
        } else {
            log.info("Fetching all work items.");
            workItems = workItemRepository.findAll();
        }
        return workItemMapper.toResponseDtoList(workItems, lang);
    }

    /**
     * UNCHANGED METHOD: Gets a single work item by its ID.
     */
    public ProjectWorkItemResponseDto getById(Long id, String lang) {
        log.info("Fetching work item with ID: {} for language: {}", id, lang);
        ProjectWorkItem workItem = workItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectWorkItem", "id", id));
        return workItemMapper.toResponseDto(workItem, lang);
    }

    /**
     * UNCHANGED METHOD: Gets all work items belonging to a specific project.
     */
    public List<ProjectWorkItemResponseDto> getByProjectId(Long projectId, String lang) {
        log.info("Fetching work items for project ID: {} and language: {}", projectId, lang);
        List<ProjectWorkItem> workItems = workItemRepository.findByProjectId(projectId);
        return workItemMapper.toResponseDtoList(workItems, lang);
    }
}

