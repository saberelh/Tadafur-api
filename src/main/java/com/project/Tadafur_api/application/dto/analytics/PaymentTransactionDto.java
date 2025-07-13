package com.project.Tadafur_api.application.dto.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a single, raw payment transaction from the database.
 */
@Data
@Builder
public class PaymentTransactionDto {
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String englishPaymentNotes;
    private String arabicPaymentNotes;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}