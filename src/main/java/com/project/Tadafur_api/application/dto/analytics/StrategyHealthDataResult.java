package com.project.Tadafur_api.application.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO Interface (Projection) to map the results from the native SQL query
 * for strategic health. This ensures the data is correctly typed when it
 * comes from the database into the Java service.
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