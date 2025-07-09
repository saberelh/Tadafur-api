// File: src/main/java/com/project/Tadafur_api/application/mapper/strategy/ProgramMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProgramResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Program;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProgramMapper {

    private static final String DEFAULT_LANG = "en";

    public ProgramResponseDto toResponseDto(Program program, String lang) {
        if (program == null) {
            return null;
        }
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        return ProgramResponseDto.builder()
                .id(program.getId())
                .parentId(program.getParentId())
                .name(getTranslatedValue(program.getNameTranslations(), languageCode))
                .description(getTranslatedValue(program.getDescriptionTranslations(), languageCode))
                .contributionPercent(program.getContributionPercent())
                .ownerId(program.getOwnerId())
                .planningStatusCode(program.getPlanningStatusCode())
                .progressStatusCode(program.getProgressStatusCode())
                .visionPriorities(program.getVisionPriorities())
                .calculatedProgressPercent(program.getCalculatedProgressPercent())
                .hybridProgressPercent(program.getHybridProgressPercent())
                .plannedTotalBudget(program.getPlannedTotalBudget())
                .calculatedTotalBudget(program.getCalculatedTotalBudget())
                .calculatedTotalPayments(program.getCalculatedTotalPayments())
                .budgetSources(program.getBudgetSources())
                .ownerNodeId(program.getOwnerNodeId())
                .build();
    }

    public List<ProgramResponseDto> toResponseDtoList(List<Program> programs, String lang) {
        return programs.stream()
                .map(p -> toResponseDto(p, lang))
                .collect(Collectors.toList());
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}