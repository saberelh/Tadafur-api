
// File: application/dto/strategy/response/StrategyResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Strategy Response DTO for API responses
 * Used by all Strategy API endpoints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResponseDto {

    private Long id;
    private String primaryName;
    private String secondaryName;
    private String primaryDescription;
    private String secondaryDescription;
    private String vision;
    private Long ownerId;
    private String ownerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timelineFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timelineTo;

    private BigDecimal plannedTotalBudget;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private BigDecimal budgetUtilization;
    private List<Integer> budgetSources;

    // Audit fields
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String lastModifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private String statusCode;

    // Calculated fields for API responses
    private Boolean isActive;
    private Boolean isCurrentlyActive;
    private Long daysRemaining;
    private String timelineStatus;

    // Aggregated data (for analytics)
    private Integer perspectiveCount;
    private Integer totalGoalCount;
    private Integer totalProjectCount;
    private BigDecimal overallProgress;

    // Display helpers
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ?
                secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    public String getFormattedBudgetUtilization() {
        return budgetUtilization != null ?
                String.format("%.2f%%", budgetUtilization) : "0.00%";
    }
}