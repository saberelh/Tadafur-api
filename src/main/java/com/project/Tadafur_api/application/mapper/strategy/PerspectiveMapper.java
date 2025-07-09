// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/PerspectiveMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.PerspectiveResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PerspectiveMapper {

    private static final String DEFAULT_LANG = "en";

    public PerspectiveResponseDto toResponseDto(Perspective perspective, String lang) {
        if (perspective == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return PerspectiveResponseDto.builder()
                .id(perspective.getId())
                .ownerId(perspective.getOwnerId())
                .parentId(perspective.getParentId())
                .name(getTranslatedValue(perspective.getNameTranslations(), languageCode))
                .description(getTranslatedValue(perspective.getDescriptionTranslations(), languageCode))
                .planningStatusCode(perspective.getPlanningStatusCode())
                .progressStatusCode(perspective.getProgressStatusCode())
                .calculatedTotalBudget(perspective.getCalculatedTotalBudget())
                .calculatedTotalPayments(perspective.getCalculatedTotalPayments())
                .plannedTotalBudget(perspective.getPlannedTotalBudget())
                .budgetSources(perspective.getBudgetSources())
                .build();
    }

    public List<PerspectiveResponseDto> toResponseDtoList(List<Perspective> perspectives, String lang) {
        return perspectives.stream()
                .map(p -> toResponseDto(p, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}