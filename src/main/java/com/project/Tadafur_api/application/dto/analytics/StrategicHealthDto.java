package com.project.Tadafur_api.application.dto.analytics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StrategicHealthDto {
    private Long strategyId;
    private String strategyName;
    private BigDecimal overallProgress; // e.g., 65.50 (representing %)
    private BigDecimal budgetVariance;  // Positive if under budget, negative if over
    private Double scheduleVarianceDays; // Average days ahead (negative) or behind (positive) schedule
    private Integer activeRisks; // Placeholder for future risk analysis
    private String healthStatus; // e.g., "On-Track", "At-Risk", "Off-Track"
}