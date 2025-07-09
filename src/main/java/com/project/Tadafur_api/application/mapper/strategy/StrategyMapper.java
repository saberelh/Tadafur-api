package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Maps Strategy entity to its DTO, handling the translation logic.
 */
@Component
public class StrategyMapper {

    private static final String DEFAULT_LANG = "en";

    public StrategyResponseDto toResponseDto(Strategy strategy, String lang) {
        if (strategy == null) {
            return null;
        }

        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return StrategyResponseDto.builder()
                .id(strategy.getId())
                .name(Optional.ofNullable(strategy.getNameTranslations())
                        .map(map -> map.getOrDefault(languageCode, map.get(DEFAULT_LANG)))
                        .orElse(null))
                .description(Optional.ofNullable(strategy.getDescriptionTranslations())
                        .map(map -> map.getOrDefault(languageCode, map.get(DEFAULT_LANG)))
                        .orElse(null))
                .vision(strategy.getVision())
                .ownerId(strategy.getOwnerId())
                .timelineFrom(strategy.getTimelineFrom())
                .timelineTo(strategy.getTimelineTo())
                .plannedTotalBudget(strategy.getPlannedTotalBudget())
                .calculatedTotalBudget(strategy.getCalculatedTotalBudget())
                .calculatedTotalPayments(strategy.getCalculatedTotalPayments())
                .build();
    }

    public List<StrategyResponseDto> toResponseDtoList(List<Strategy> strategies, String lang) {
        if (strategies == null || strategies.isEmpty()) {
            return Collections.emptyList();
        }
        return strategies.stream()
                .map(strategy -> toResponseDto(strategy, lang))
                .collect(Collectors.toList());
    }
}