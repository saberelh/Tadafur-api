// File: src/main/java/com/project/Tadafur_api/application/service/strategy/HierarchyService.java
package com.project.Tadafur_api.application.service.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * NEW METHOD: Finds all strategies for a given owner and builds a full
     * hierarchy for each one.
     *
     * @param ownerId The ID of the owner (Authority).
     * @param lang The language code for translation.
     * @return A list of fully populated strategy hierarchies.
     */
    public List<StrategyHierarchyDto> getHierarchiesByOwner(Long ownerId, String lang) {
        log.info("Fetching all strategy hierarchies for owner ID: {}", ownerId);

        // 1. Find all strategies belonging to the specified owner.
        List<Strategy> ownerStrategies = strategyRepository.findByOwnerId(ownerId);

        if (ownerStrategies.isEmpty()) {
            log.warn("No strategies found for owner ID: {}", ownerId);
            return Collections.emptyList();
        }

        // 2. For each strategy, call the existing getFullHierarchy method and collect the results.
        return ownerStrategies.stream()
                .map(strategy -> getFullHierarchy(strategy.getId(), lang))
                .collect(Collectors.toList());
    }

    /**
     * UNCHANGED METHOD: Gets the full hierarchy for a single strategy.
     * This is now a reusable helper method for the new getHierarchiesByOwner method.
     */
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

    // --- All private helper methods for building the hierarchy remain unchanged ---

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

    public List<StrategyHierarchyDto> getFastFullHierarchy(Long strategyId, Long ownerId, String lang) {
        log.info("Building full hierarchy efficiently with strategyId: {} and ownerId: {}", strategyId, ownerId);
        List<StrategyRepository.FlatHierarchyResult> flatData = strategyRepository.getFlatHierarchy(strategyId, ownerId);

        if (flatData.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<StrategyRepository.FlatHierarchyResult>> groupedByStrategy = flatData.stream()
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getStrategyId));

        return groupedByStrategy.values().stream()
                .map(strategyData -> buildStrategy(strategyData, lang))
                .collect(Collectors.toList());
    }

    private StrategyHierarchyDto buildStrategy(List<StrategyRepository.FlatHierarchyResult> strategyData, String lang) {
        StrategyRepository.FlatHierarchyResult firstRecord = strategyData.get(0);
        Map<Long, List<StrategyRepository.FlatHierarchyResult>> perspectives = strategyData.stream()
                .filter(r -> r.getPerspectiveId() != null)
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getPerspectiveId));
        return StrategyHierarchyDto.builder()
                .id(firstRecord.getStrategyId())
                .name(getTranslatedValue(firstRecord.getStrategyName(), lang))
                .perspectives(perspectives.values().stream().map(data -> buildPerspective(data, lang)).collect(Collectors.toList()))
                .build();
    }

    private PerspectiveHierarchyDto buildPerspective(List<StrategyRepository.FlatHierarchyResult> perspectiveData, String lang) {
        StrategyRepository.FlatHierarchyResult firstRecord = perspectiveData.get(0);
        Map<Long, List<StrategyRepository.FlatHierarchyResult>> goals = perspectiveData.stream()
                .filter(r -> r.getGoalId() != null)
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getGoalId));
        return PerspectiveHierarchyDto.builder()
                .id(firstRecord.getPerspectiveId())
                .name(getTranslatedValue(firstRecord.getPerspectiveName(), lang))
                .goals(goals.values().stream().map(data -> buildGoal(data, lang)).collect(Collectors.toList()))
                .build();
    }

    private GoalHierarchyDto buildGoal(List<StrategyRepository.FlatHierarchyResult> goalData, String lang) {
        StrategyRepository.FlatHierarchyResult firstRecord = goalData.get(0);
        Map<Long, List<StrategyRepository.FlatHierarchyResult>> programs = goalData.stream()
                .filter(r -> r.getProgramId() != null)
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getProgramId));
        return GoalHierarchyDto.builder()
                .id(firstRecord.getGoalId())
                .name(getTranslatedValue(firstRecord.getGoalName(), lang))
                .programs(programs.values().stream().map(data -> buildProgram(data, lang)).collect(Collectors.toList()))
                .build();
    }

    private ProgramHierarchyDto buildProgram(List<StrategyRepository.FlatHierarchyResult> programData, String lang) {
        StrategyRepository.FlatHierarchyResult firstRecord = programData.get(0);
        Map<Long, List<StrategyRepository.FlatHierarchyResult>> initiatives = programData.stream()
                .filter(r -> r.getInitiativeId() != null)
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getInitiativeId));
        return ProgramHierarchyDto.builder()
                .id(firstRecord.getProgramId())
                .name(getTranslatedValue(firstRecord.getProgramName(), lang))
                .initiatives(initiatives.values().stream().map(data -> buildInitiative(data, lang)).collect(Collectors.toList()))
                .build();
    }

    private InitiativeHierarchyDto buildInitiative(List<StrategyRepository.FlatHierarchyResult> initiativeData, String lang) {
        StrategyRepository.FlatHierarchyResult firstRecord = initiativeData.get(0);
        Map<Long, List<StrategyRepository.FlatHierarchyResult>> projects = initiativeData.stream()
                .filter(r -> r.getProjectId() != null)
                .collect(Collectors.groupingBy(StrategyRepository.FlatHierarchyResult::getProjectId));
        return InitiativeHierarchyDto.builder()
                .id(firstRecord.getInitiativeId())
                .name(getTranslatedValue(firstRecord.getInitiativeName(), lang))
                .projects(projects.values().stream().map(data -> buildProject(data.get(0), lang)).collect(Collectors.toList()))
                .build();
    }

    private ProjectHierarchyDto buildProject(StrategyRepository.FlatHierarchyResult projectData, String lang) {
        return ProjectHierarchyDto.builder()
                .id(projectData.getProjectId())
                .name(getTranslatedValue(projectData.getProjectName(), lang))
                .build();
    }

    private String getTranslatedValue(String json, String lang) {
        if (json == null || json.isEmpty()) return "Unknown";
        try {
            Map<String, String> translations = new ObjectMapper().readValue(json, new TypeReference<>() {});
            return translations.getOrDefault(lang, translations.get("en"));
        } catch (Exception e) {
            log.error("Error parsing name_translations JSON: {}", json, e);
            return "Unknown";
        }
    }
}
