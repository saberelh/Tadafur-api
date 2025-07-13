package com.project.Tadafur_api.domain.strategy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a single payment transaction in the budget_payments table.
 * This entity is now correctly annotated for JPA management and contains all
 * the necessary fields and getter methods.
 */
@Entity
@Table(name = "budget_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetPayment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "entity_code", nullable = false)
    private String entityCode;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private BigDecimal amount;

    // These fields correspond to the 'english_payment_notes' and 'arabic_payment_notes' columns.
    // I have used the names from your provided database sample. Please adjust if they are different in your table.
    @Column(name = "english_payment_notes", columnDefinition = "TEXT")
    private String primaryPaymentNotes; // Corresponds to english_payment_notes

    @Column(name = "arabic_payment_notes", columnDefinition = "TEXT")
    private String secondaryPaymentNotes; // Corresponds to arabic_payment_notes

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "status_code")
    private String statusCode;
}