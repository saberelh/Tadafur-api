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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    /**
     * Gets a single project by its ID, translated to the specified language.
     */
    public ProjectResponseDto getById(Long id, String lang) {
        log.info("Fetching project with ID: {} for language: {}", id, lang);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.toResponseDto(project, lang);
    }

    /**
     * Gets all projects belonging to a specific initiative, translated.
     */
    public List<ProjectResponseDto> getByInitiativeId(Long initiativeId, String lang) {
        log.info("Fetching projects for initiative ID: {} and language: {}", initiativeId, lang);
        List<Project> projects = projectRepository.findByParentId(initiativeId);
        return projectMapper.toResponseDtoList(projects, lang);
    }
}