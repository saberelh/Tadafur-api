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
public class ProjectResponseDto {

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

    private BigDecimal actualCost;
    private Long priorityId;
    private String priorityName;
    private Long statusId;
    private String statusName;

    private List<Integer> visionPriorities;
    private List<String> visionPriorityNames;

    private Long projectMethodologyId;
    private String projectMethodologyName;

    private BigDecimal progressByEffort;
    private BigDecimal progressByAverage;
    private Long progressSpecificationId;
    private Long propagationModelId;
    private BigDecimal manualProgressByEffort;
    private BigDecimal manualProgressByAverage;
    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal effectiveProgress;

    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private BigDecimal budgetUtilization;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime summarySentDate;

    private Long summaryPeriod;
    private String summaryPeriodName;
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
    private Integer workItemCount;
    private Integer completedWorkItemCount;
    private Integer memberCount;
    private Integer stakeholderCount;

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