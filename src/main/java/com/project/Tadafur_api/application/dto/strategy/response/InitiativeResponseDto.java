// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/InitiativeResponseDto.java
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
public class InitiativeResponseDto {
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
    private int[] visionPriorities;
    private BigDecimal calculatedProgressPercent;
    private BigDecimal hybridProgressPercent;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private Integer ownerNodeId;
    private int[] budgetSources;
}