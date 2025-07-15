package com.project.Tadafur_api.application.dto.analytics;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * The main response object for a single project's detailed spending analysis.
 * The API will return a LIST of these objects.
 */
@Data
@Builder
public class ProjectSpendingDetailsDto {
    private Long projectId;
    private String projectName;
    private SpendingSummaryDto summary;
    private List<PaymentTransactionDto> payments;
}