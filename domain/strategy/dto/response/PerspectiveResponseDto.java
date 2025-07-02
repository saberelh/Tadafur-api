package com.project.Tadafur_api.domain.strategy.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerspectiveResponseDto {

    private Long id;
    private String primaryName;
    private String secondaryName;
    private String primaryDescription;
    private String secondaryDescription;
    private Long ownerId;
    private String ownerName;
    private Long parentId;
    private String parentName;

    private String planningStatusCode;
    private String progressStatusCode;

    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private BigDecimal plannedTotalBudget;
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
    private Integer goalCount;
    private Integer totalInitiativeCount;
    private Integer totalProjectCount;
    private BigDecimal averageProgress;

    // Display helpers
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }
}