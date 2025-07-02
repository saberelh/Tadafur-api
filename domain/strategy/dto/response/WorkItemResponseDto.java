package com.project.Tadafur_api.domain.strategy.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkItemResponseDto {

    private Long id;
    private Long projectId;
    private String projectName;
    private Long parentId;
    private String parentName;

    private String primaryName;
    private String secondaryName;
    private String primaryDescription;
    private String secondaryDescription;

    private Long priorityId;
    private String priorityName;
    private Long statusId;
    private String statusName;

    private Long assigneeUserId;
    private String assigneeName;

    private BigDecimal estimatedTime;
    private Integer estimatedTimeUnit;
    private String estimatedTimeUnitName;
    private BigDecimal actualTime;
    private Integer actualTimeUnit;
    private String actualTimeUnitName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedDueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualDueDate;

    private BigDecimal progressPercent;

    private Long workItemGroupId;
    private String workItemGroupName;
    private Integer level;
    private Integer itemSort;

    private String verificationResult;

    private BigDecimal progressByEffort;
    private BigDecimal progressByAverage;
    private BigDecimal manualProgressByEffort;
    private BigDecimal manualProgressByAverage;
    private BigDecimal effectiveProgress;

    private Boolean isAddedFromCustom;
    private Boolean isCompleted;
    private Boolean isOverdue;

    // Audit fields
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String lastModifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private String statusCode;

    // Aggregated data
    private Integer subTaskCount;
    private Integer completedSubTaskCount;
    private Integer attachmentCount;
    private Integer dependencyCount;

    // Display helpers
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    public String getFormattedProgress() {
        return effectiveProgress != null ? String.format("%.1f%%", effectiveProgress) : "0.0%";
    }

    public String getTimeStatus() {
        if (isCompleted != null && isCompleted) {
            return "COMPLETED";
        } else if (isOverdue != null && isOverdue) {
            return "OVERDUE";
        } else if (plannedDueDate != null && plannedDueDate.equals(LocalDate.now())) {
            return "DUE_TODAY";
        } else if (plannedDueDate != null && plannedDueDate.isAfter(LocalDate.now())) {
            return "ON_TRACK";
        }
        return "UNKNOWN";
    }
}