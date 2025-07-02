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
public class InitiativeResponseDto {

    private Long id;
    private String primaryName;
    private String secondaryName;
    private String primaryDescription;
    private String secondaryDescription;
    private Long parentId;
    private String parentName;
    private Double contributionPercent;
    private Long ownerId;
    private String ownerName;

    private BigDecimal plannedTotalBudget;
    private Integer type;
    private String typeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String planningStatusCode;
    private String progressStatusCode;

    private List<Integer> visionPriorities;
    private List<String> visionPriorityNames;

    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal effectiveProgress;

    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private BigDecimal budgetUtilization;

    private Long ownerNodeId;
    private String ownerNodeName;
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
    private Integer projectCount;
    private Integer activeProjectCount;
    private Integer completedProjectCount;
    private Integer indicatorCount;
    private Integer contributorCount;

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