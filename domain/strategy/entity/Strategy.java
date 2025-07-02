package com.project.Tadafur_api.domain.strategy.entity;

import com.project.Tadafur_api.shared.common.entity.BaseEntity;
import jakarta.persistence.*;
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

    @Column(name = "primary_name")
    private String primaryName;

    @Column(name = "secondary_name")
    private String secondaryName;

    @Column(name = "primary_description", columnDefinition = "TEXT")
    private String primaryDescription;

    @Column(name = "secondary_description", columnDefinition = "TEXT")
    private String secondaryDescription;

    @Column(name = "vision", columnDefinition = "TEXT")
    private String vision;

    @Column(name = "owner_id")
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
    private String budgetSources;

    // Helper methods
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ?
                secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
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

    public boolean isWithinTimeline(LocalDate date) {
        if (timelineFrom == null || timelineTo == null || date == null) {
            return false;
        }
        return !date.isBefore(timelineFrom) && !date.isAfter(timelineTo);
    }
}