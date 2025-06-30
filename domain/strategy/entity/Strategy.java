package com.project.Tadafur_api.domain.strategy.entity;

import com.project.Tadafur_api.shared.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "strategy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Strategy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "arabic_name", nullable = false)
    private String arabicName;

    @NotNull
    @Size(max = 255)
    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Lob
    @Column(name = "arabic_description")
    private String arabicDescription;

    @Lob
    @Column(name = "english_description")
    private String englishDescription;

    @Lob
    @Column(name = "vision")
    private String vision;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "timeline_from")
    private LocalDate timelineFrom;

    @Column(name = "timeline_to")
    private LocalDate timelineTo;

    @Column(name = "planned_total_budget", precision = 15, scale = 2)
    private BigDecimal plannedTotalBudget;

    @Column(name = "calculated_total_budget", precision = 15, scale = 2)
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments", precision = 15, scale = 2)
    private BigDecimal calculatedTotalPayments;

    @Column(name = "budget_sources")
    private String budgetSources; // JSON array as string

    // Helper methods
    public boolean isWithinTimeline(LocalDate date) {
        if (timelineFrom == null || timelineTo == null || date == null) {
            return false;
        }
        return !date.isBefore(timelineFrom) && !date.isAfter(timelineTo);
    }

    public BigDecimal getBudgetUtilization() {
        if (plannedTotalBudget == null || plannedTotalBudget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (calculatedTotalPayments == null) {
            return BigDecimal.ZERO;
        }
        return calculatedTotalPayments.divide(plannedTotalBudget, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}