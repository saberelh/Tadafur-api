package com.project.Tadafur_api.domain.strategy.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponseDto {

    private Long id;
    private String primaryName;
    private String secondaryName;
    private String primaryDescription;
    private String secondaryDescription;
    private Long parentId;
    private String parentName;
    private Long ownerId;
    private String ownerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String planningStatusCode;
    private String progressStatusCode;

    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal effectiveProgress;

    private Long visionPriority;
    private String visionPriorityName;

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

    // Aggregated data
    private Integer programCount;
    private Integer totalInitiativeCount;
    private Integer totalProjectCount;
    private Integer indicatorCount;
    private Integer kpiCount;

    // Timeline status
    private Boolean isActive;
    private Long daysRemaining;
    private String timelineStatus;

    // Display helpers
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }
}