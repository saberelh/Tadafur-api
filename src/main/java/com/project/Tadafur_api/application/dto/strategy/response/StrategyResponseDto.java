package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for API responses. Carries translated fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StrategyResponseDto {

    private Long id;
    private String name;
    private String description;
    private String vision;
    private Long ownerId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timelineFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timelineTo;

    private BigDecimal plannedTotalBudget;
    private BigDecimal calculatedTotalBudget;
    private BigDecimal calculatedTotalPayments;
    private List<Integer> budgetSources;
}