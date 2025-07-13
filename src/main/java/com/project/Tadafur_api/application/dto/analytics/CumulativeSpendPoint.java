package com.project.Tadafur_api.application.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CumulativeSpendPoint {
    Long getProjectId();
    LocalDate getPaymentDate();
    BigDecimal getCumulativeSpend();
}