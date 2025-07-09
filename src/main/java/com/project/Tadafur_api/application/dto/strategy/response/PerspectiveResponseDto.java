// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/PerspectiveResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerspectiveResponseDto {
    private Long id;
    private Long ownerId;
    private Long parentId;
    private String name;
    private String description;
    private String planningStatusCode;
    private String progressStatusCode;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private BigDecimal plannedTotalBudget;
    private int[] budgetSources;
}