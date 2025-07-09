// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/ProjectMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private static final String DEFAULT_LANG = "en";

    public ProjectResponseDto toResponseDto(Project project, String lang) {
        if (project == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return ProjectResponseDto.builder()
                .id(project.getId())
                .parentId(project.getParentId())
                .name(getTranslatedValue(project.getNameTranslations(), languageCode))
                .description(getTranslatedValue(project.getDescriptionTranslations(), languageCode))
                .contributionPercent(project.getContributionPercent())
                .ownerId(project.getOwnerId())
                .plannedTotalBudget(project.getPlannedTotalBudget())
                .type(project.getType())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .planningStatusCode(project.getPlanningStatusCode())
                .progressStatusCode(project.getProgressStatusCode())
                .actualCost(project.getActualCost())
                .priorityId(project.getPriorityId())
                .statusId(project.getStatusId())
                .visionPriorities(project.getVisionPriorities())
                .projectMethodologyId(project.getProjectMethodologyId())
                .progressByEffort(project.getProgressByEffort())
                .progressByAverage(project.getProgressByAverage())
                .progressSpecificationId(project.getProgressSpecificationId())
                .propagationModelId(project.getPropagationModelId())
                .manualProgressByEffort(project.getManualProgressByEffort())
                .manualProgressByAverage(project.getManualProgressByAverage())
                .calculatedProgressPercent(project.getCalculatedProgressPercent())
                .hybridProgressPercent(project.getHybridProgressPercent())
                .calculatedTotalBudget(project.getCalculatedTotalBudget())
                .calculatedTotalPayments(project.getCalculatedTotalPayments())
                .summarySentDate(project.getSummarySentDate())
                .summaryPeriod(project.getSummaryPeriod())
                .budgetSources(project.getBudgetSources())
                .ownerNodeId(project.getOwnerNodeId())
                .build();
    }

    public List<ProjectResponseDto> toResponseDtoList(List<Project> projects, String lang) {
        return projects.stream()
                .map(p -> toResponseDto(p, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}