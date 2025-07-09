// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/ProjectResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponseDto {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private Double contributionPercent;
    private Long ownerId;
    private BigDecimal plannedTotalBudget;
    private Integer type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String planningStatusCode;
    private String progressStatusCode;
    private BigDecimal actualCost;
    private Integer priorityId;
    private Integer statusId;
    private int[] visionPriorities;
    private Long projectMethodologyId;
    private BigDecimal progressByEffort;
    private BigDecimal progressByAverage;
    private Integer progressSpecificationId;
    private Integer propagationModelId;
    private BigDecimal manualProgressByEffort;
    private BigDecimal manualProgressByAverage;
    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime summarySentDate;
    private Integer summaryPeriod;
    private int[] budgetSources;
    private Integer ownerNodeId;
}