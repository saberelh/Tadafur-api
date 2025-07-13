package com.project.Tadafur_api.application.dto.analytics;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * A nested DTO to hold the summary budget data for a project.
 */
@Data
@Builder
public class SpendingSummaryDto {
    private BigDecimal plannedTotalBudget;
    private BigDecimal paymentBudget;
}