// File: src/main/java/com/project/Tadafur_api/application/service/strategy/ProjectService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.ProjectMapper;
import com.project.Tadafur_api.domain.strategy.entity.Project;
import com.project.Tadafur_api.domain.strategy.repository.ProjectRepository;
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
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    /**
     * NEW METHOD: Gets projects based on an optional owner ID.
     */
    public List<ProjectResponseDto> getProjects(Optional<Long> ownerId, String lang) {
        List<Project> projects;
        if (ownerId.isPresent()) {
            log.info("Fetching projects for owner ID: {}", ownerId.get());
            projects = projectRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Fetching all projects.");
            projects = projectRepository.findAll();
        }
        return projectMapper.toResponseDtoList(projects, lang);
    }

    /**
     * UNCHANGED METHOD: Gets a single project by its ID.
     */
    public ProjectResponseDto getById(Long id, String lang) {
        log.info("Fetching project with ID: {} for language: {}", id, lang);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.toResponseDto(project, lang);
    }

    /**
     * UNCHANGED METHOD: Gets all projects belonging to a specific initiative.
     */
    public List<ProjectResponseDto> getByInitiativeId(Long initiativeId, String lang) {
        log.info("Fetching projects for initiative ID: {} and language: {}", initiativeId, lang);
        List<Project> projects = projectRepository.findByParentId(initiativeId);
        return projectMapper.toResponseDtoList(projects, lang);
    }
}
