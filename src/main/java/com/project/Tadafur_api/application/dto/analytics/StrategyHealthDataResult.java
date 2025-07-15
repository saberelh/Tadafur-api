package com.project.Tadafur_api.application.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A flat DTO to hold the combined results for the strategic health query.
 * Each instance represents one project's contribution to a strategy's health.
 */
public interface StrategyHealthDataResult {
    Long getStrategyId();
    String getNameTranslations();
    String getPlanningStatusCode();
    String getProgressStatusCode();
    BigDecimal getCalculatedProgressPercent();
    BigDecimal getPlannedTotalBudget();
    BigDecimal getActualCost();
    LocalDate getEndDate();
}