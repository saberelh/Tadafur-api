// File: src/main/java/com/project/Tadafur_api/application/service/strategy/HierarchyService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.hierarchy.*;
import com.project.Tadafur_api.domain.strategy.entity.*;
import com.project.Tadafur_api.domain.strategy.repository.*;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HierarchyService {

    private final StrategyRepository strategyRepository;
    private final PerspectiveRepository perspectiveRepository;
    private final GoalRepository goalRepository;
    private final ProgramRepository programRepository;
    private final InitiativeRepository initiativeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectWorkItemRepository projectWorkItemRepository;

    private static final String DEFAULT_LANG = "en";

    public StrategyHierarchyDto getFullHierarchy(Long strategyId, String lang) {
        log.info("Building full hierarchy for Strategy ID: {}", strategyId);
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", strategyId));

        return StrategyHierarchyDto.builder()
                .id(strategy.getId())
                .name(getTranslatedValue(strategy.getNameTranslations(), languageCode))
                .vision(strategy.getVision())
                .perspectives(buildPerspectiveHierarchy(strategy.getId(), languageCode))
                .build();
    }

    private List<PerspectiveHierarchyDto> buildPerspectiveHierarchy(Long strategyId, String lang) {
        List<Perspective> perspectives = perspectiveRepository.findByParentId(strategyId);
        if (perspectives.isEmpty()) return Collections.emptyList();
        return perspectives.stream().map(p -> PerspectiveHierarchyDto.builder()
                .id(p.getId())
                .name(getTranslatedValue(p.getNameTranslations(), lang))
                .goals(buildGoalHierarchy(p.getId(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private List<GoalHierarchyDto> buildGoalHierarchy(Long perspectiveId, String lang) {
        List<Goal> goals = goalRepository.findByParentId(perspectiveId);
        if (goals.isEmpty()) return Collections.emptyList();
        return goals.stream().map(g -> GoalHierarchyDto.builder()
                .id(g.getId())
                .name(getTranslatedValue(g.getNameTranslations(), lang))
                .programs(buildProgramHierarchy(g.getId(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private List<ProgramHierarchyDto> buildProgramHierarchy(Long goalId, String lang) {
        List<Program> programs = programRepository.findByParentId(goalId);
        if (programs.isEmpty()) return Collections.emptyList();
        return programs.stream().map(p -> ProgramHierarchyDto.builder()
                .id(p.getId())
                .name(getTranslatedValue(p.getNameTranslations(), lang))
                .initiatives(buildInitiativeHierarchy(p.getId(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private List<InitiativeHierarchyDto> buildInitiativeHierarchy(Long programId, String lang) {
        List<Initiative> initiatives = initiativeRepository.findByParentId(programId);
        if (initiatives.isEmpty()) return Collections.emptyList();
        return initiatives.stream().map(i -> InitiativeHierarchyDto.builder()
                .id(i.getId())
                .name(getTranslatedValue(i.getNameTranslations(), lang))
                .projects(buildProjectHierarchy(i.getId(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private List<ProjectHierarchyDto> buildProjectHierarchy(Long initiativeId, String lang) {
        List<Project> projects = projectRepository.findByParentId(initiativeId);
        if (projects.isEmpty()) return Collections.emptyList();
        return projects.stream().map(p -> ProjectHierarchyDto.builder()
                .id(p.getId())
                .name(getTranslatedValue(p.getNameTranslations(), lang))
                .workItems(buildWorkItemHierarchy(p.getId(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private List<ProjectWorkItemHierarchyDto> buildWorkItemHierarchy(Long projectId, String lang) {
        List<ProjectWorkItem> workItems = projectWorkItemRepository.findByProjectId(projectId);
        if (workItems.isEmpty()) return Collections.emptyList();
        return workItems.stream().map(wi -> ProjectWorkItemHierarchyDto.builder()
                .id(wi.getId())
                .name(getTranslatedValue(wi.getNameTranslations(), lang))
                .build()
        ).collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}

