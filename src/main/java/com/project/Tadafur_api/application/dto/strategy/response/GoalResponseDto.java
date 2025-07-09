// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/GoalResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalResponseDto {
    private Long id;
    private Long parentId;
    private Long ownerId;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String planningStatusCode;
    private String progressStatusCode;
    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private Integer visionPriority;
    private BigDecimal plannedTotalBudget;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private int[] budgetSources;
    private Integer ownerNodeId;
}