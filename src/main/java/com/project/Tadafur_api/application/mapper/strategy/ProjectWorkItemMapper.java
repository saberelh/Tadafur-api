// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/ProjectWorkItemMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProjectWorkItemResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.ProjectWorkItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectWorkItemMapper {

    private static final String DEFAULT_LANG = "en";

    public ProjectWorkItemResponseDto toResponseDto(ProjectWorkItem workItem, String lang) {
        if (workItem == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return ProjectWorkItemResponseDto.builder()
                .id(workItem.getId())
                .projectId(workItem.getProjectId())
                .parentId(workItem.getParentId())
                .name(getTranslatedValue(workItem.getNameTranslations(), languageCode))
                .description(getTranslatedValue(workItem.getDescriptionTranslations(), languageCode))
                .priorityId(workItem.getPriorityId())
                .statusId(workItem.getStatusId())
                .assigneeUserId(workItem.getAssigneeUserId())
                .estimatedTime(workItem.getEstimatedTime())
                .estimatedTimeUnit(workItem.getEstimatedTimeUnit())
                .actualTime(workItem.getActualTime())
                .actualTimeUnit(workItem.getActualTimeUnit())
                .plannedStartDate(workItem.getPlannedStartDate())
                .plannedDueDate(workItem.getPlannedDueDate())
                .actualStartDate(workItem.getActualStartDate())
                .actualDueDate(workItem.getActualDueDate())
                .progressPercent(workItem.getProgressPercent())
                .workItemGroupId(workItem.getWorkItemGroupId())
                .level(workItem.getLevel())
                .itemSort(workItem.getItemSort())
                .verificationResult(workItem.getVerificationResult())
                .progressByEffort(workItem.getProgressByEffort())
                .progressByAverage(workItem.getProgressByAverage())
                .manualProgressByEffort(workItem.getManualProgressByEffort())
                .manualProgressByAverage(workItem.getManualProgressByAverage())
                .isAddedFromCustom(workItem.getIsAddedFromCustom())
                .build();
    }

    public List<ProjectWorkItemResponseDto> toResponseDtoList(List<ProjectWorkItem> workItems, String lang) {
        return workItems.stream()
                .map(wi -> toResponseDto(wi, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}