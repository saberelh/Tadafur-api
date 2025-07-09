// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/ProgramResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramResponseDto {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private Double contributionPercent;
    private Long ownerId;
    private String planningStatusCode;
    private String progressStatusCode;
    private int[] visionPriorities;
    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal plannedTotalBudget;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private int[] budgetSources;
    private Integer ownerNodeId;
}