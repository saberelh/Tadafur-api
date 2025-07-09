// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/GoalMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.GoalResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Goal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GoalMapper {

    private static final String DEFAULT_LANG = "en";

    public GoalResponseDto toResponseDto(Goal goal, String lang) {
        if (goal == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return GoalResponseDto.builder()
                .id(goal.getId())
                .parentId(goal.getParentId())
                .ownerId(goal.getOwnerId())
                .name(getTranslatedValue(goal.getNameTranslations(), languageCode))
                .description(getTranslatedValue(goal.getDescriptionTranslations(), languageCode))
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .planningStatusCode(goal.getPlanningStatusCode())
                .progressStatusCode(goal.getProgressStatusCode())
                .calculatedProgressPercent(goal.getCalculatedProgressPercent())
                .hybridProgressPercent(goal.getHybridProgressPercent())
                .visionPriority(goal.getVisionPriority())
                .plannedTotalBudget(goal.getPlannedTotalBudget())
                .calculatedTotalBudget(goal.getCalculatedTotalBudget())
                .calculatedTotalPayments(goal.getCalculatedTotalPayments())
                .budgetSources(goal.getBudgetSources())
                .ownerNodeId(goal.getOwnerNodeId())
                .build();
    }

    public List<GoalResponseDto> toResponseDtoList(List<Goal> goals, String lang) {
        return goals.stream()
                .map(g -> toResponseDto(g, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}