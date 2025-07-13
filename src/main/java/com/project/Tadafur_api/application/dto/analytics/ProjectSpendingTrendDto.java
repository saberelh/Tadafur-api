package com.project.Tadafur_api.application.dto.analytics;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProjectSpendingTrendDto {
    private Long projectId;
    private String projectName;
    private BigDecimal plannedTotalBudget;
    private List<PaymentDto> payments;
    private List<DataPoint> dataPoints;

    @Getter
    @Builder
    public static class DataPoint {
        private LocalDate date;
        private BigDecimal cumulativeActualSpend;
        private BigDecimal cumulativePlannedSpend;
    }
}