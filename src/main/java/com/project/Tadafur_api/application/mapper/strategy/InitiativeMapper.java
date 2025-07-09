// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/InitiativeMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.InitiativeResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Initiative;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InitiativeMapper {

    private static final String DEFAULT_LANG = "en";

    public InitiativeResponseDto toResponseDto(Initiative initiative, String lang) {
        if (initiative == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return InitiativeResponseDto.builder()
                .id(initiative.getId())
                .parentId(initiative.getParentId())
                .name(getTranslatedValue(initiative.getNameTranslations(), languageCode))
                .description(getTranslatedValue(initiative.getDescriptionTranslations(), languageCode))
                .contributionPercent(initiative.getContributionPercent())
                .ownerId(initiative.getOwnerId())
                .plannedTotalBudget(initiative.getPlannedTotalBudget())
                .type(initiative.getType())
                .startDate(initiative.getStartDate())
                .endDate(initiative.getEndDate())
                .planningStatusCode(initiative.getPlanningStatusCode())
                .progressStatusCode(initiative.getProgressStatusCode())
                .visionPriorities(initiative.getVisionPriorities())
                .calculatedProgressPercent(initiative.getCalculatedProgressPercent())
                .hybridProgressPercent(initiative.getHybridProgressPercent())
                .calculatedTotalBudget(initiative.getCalculatedTotalBudget())
                .calculatedTotalPayments(initiative.getCalculatedTotalPayments())
                .ownerNodeId(initiative.getOwnerNodeId())
                .budgetSources(initiative.getBudgetSources())
                .build();
    }

    public List<InitiativeResponseDto> toResponseDtoList(List<Initiative> initiatives, String lang) {
        return initiatives.stream()
                .map(i -> toResponseDto(i, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}