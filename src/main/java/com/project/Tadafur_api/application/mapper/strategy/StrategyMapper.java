// File: application/mapper/strategy/StrategyMapper.java
package com.project.Tadafur_api.application.mapper.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Mapper - Convert between Strategy entity and DTOs
 */
@Component
public class StrategyMapper {

    /**
     * Convert Strategy entity to StrategyResponseDto
     */
    public StrategyResponseDto toResponseDto(Strategy strategy) {
        if (strategy == null) {
            return null;
        }

        StrategyResponseDto dto = StrategyResponseDto.builder()
                .id(strategy.getId())
                .primaryName(strategy.getPrimaryName())
                .secondaryName(strategy.getSecondaryName())
                .primaryDescription(strategy.getPrimaryDescription())
                .secondaryDescription(strategy.getSecondaryDescription())
                .vision(strategy.getVision())
                .ownerId(strategy.getOwnerId())
                .timelineFrom(strategy.getTimelineFrom())
                .timelineTo(strategy.getTimelineTo())
                .plannedTotalBudget(strategy.getPlannedTotalBudget())
                .calculatedTotalBudget(strategy.getCalculatedTotalBudget())
                .calculatedTotalPayments(strategy.getCalculatedTotalPayments())
                .budgetUtilization(strategy.getBudgetUtilization())
                .createdBy(strategy.getCreatedBy())
                .createdAt(strategy.getCreatedAt())
                .lastModifiedBy(strategy.getLastModifiedBy())
                .lastModifiedAt(strategy.getLastModifiedAt())
                .statusCode(strategy.getStatusCode())
                .build();

        // Calculate derived fields
        enrichWithCalculatedFields(dto, strategy);

        // Parse budget sources if present
        if (strategy.getBudgetSources() != null && !strategy.getBudgetSources().trim().isEmpty()) {
            try {
                // Convert JSON string to list of integers
                String sources = strategy.getBudgetSources().trim();
                if (sources.startsWith("[") && sources.endsWith("]")) {
                    sources = sources.substring(1, sources.length() - 1);
                    if (!sources.isEmpty()) {
                        List<Integer> budgetSourcesList = List.of(sources.split(","))
                                .stream()
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        dto.setBudgetSources(budgetSourcesList);
                    }
                }
            } catch (Exception e) {
                // If parsing fails, set empty list
                dto.setBudgetSources(List.of());
            }
        }

        return dto;
    }

    /**
     * Convert list of Strategy entities to list of DTOs
     */
    public List<StrategyResponseDto> toResponseDtoList(List<Strategy> strategies) {
        if (strategies == null) {
            return null;
        }
        return strategies.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Enrich DTO with calculated fields
     */
    private void enrichWithCalculatedFields(StrategyResponseDto dto, Strategy strategy) {
        LocalDate now = LocalDate.now();

        // Set active status
        dto.setIsActive(strategy.isActive());
        dto.setIsCurrentlyActive(strategy.isCurrentlyActive());

        // Calculate days remaining
        dto.setDaysRemaining(strategy.getDaysRemaining());

        // Determine timeline status
        dto.setTimelineStatus(determineTimelineStatus(strategy, now));

        // Set default values for aggregated data (will be populated by service layer if needed)
        dto.setPerspectiveCount(0);
        dto.setTotalGoalCount(0);
        dto.setTotalProjectCount(0);
        dto.setOverallProgress(java.math.BigDecimal.ZERO);
    }

    /**
     * Determine timeline status based on dates
     */
    private String determineTimelineStatus(Strategy strategy, LocalDate now) {
        if (strategy.getTimelineFrom() == null || strategy.getTimelineTo() == null) {
            return "NO_TIMELINE";
        }

        if (now.isBefore(strategy.getTimelineFrom())) {
            return "FUTURE";
        } else if (now.isAfter(strategy.getTimelineTo())) {
            return "EXPIRED";
        } else {
            // Check if approaching deadline (within 30 days)
            long daysRemaining = strategy.getDaysRemaining() != null ? strategy.getDaysRemaining() : 0;
            if (daysRemaining <= 30 && daysRemaining > 0) {
                return "APPROACHING_DEADLINE";
            } else if (daysRemaining <= 0) {
                return "OVERDUE";
            } else {
                return "ACTIVE";
            }
        }
    }

    /**
     * Convert Strategy entity to minimal DTO (for nested objects)
     */
    public StrategyResponseDto toMinimalDto(Strategy strategy) {
        if (strategy == null) {
            return null;
        }

        return StrategyResponseDto.builder()
                .id(strategy.getId())
                .primaryName(strategy.getPrimaryName())
                .secondaryName(strategy.getSecondaryName())
                .ownerId(strategy.getOwnerId())
                .timelineFrom(strategy.getTimelineFrom())
                .timelineTo(strategy.getTimelineTo())
                .statusCode(strategy.getStatusCode())
                .isActive(strategy.isActive())
                .isCurrentlyActive(strategy.isCurrentlyActive())
                .build();
    }
}