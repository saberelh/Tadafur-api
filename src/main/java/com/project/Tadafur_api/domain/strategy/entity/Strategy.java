// File: domain/strategy/entity/Strategy.java
package com.project.Tadafur_api.domain.strategy.entity;

import com.project.Tadafur_api.domain.common.entity.BaseEntity;
import com.project.Tadafur_api.shared.constants.DomainConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Strategy Entity - Top level of the strategic planning hierarchy
 * Strategy → Perspective → Goal → Program → Initiative → Project → Work Items
 */
@Entity
@Table(name = "strategy", schema = DomainConstants.DATABASE_SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Strategy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Primary name is required")
    @Size(max = 255)
    @Column(name = "primary_name", nullable = false)
    private String primaryName;

    @Size(max = 255)
    @Column(name = "secondary_name")
    private String secondaryName;

    @Column(name = "primary_description", columnDefinition = "TEXT")
    private String primaryDescription;

    @Column(name = "secondary_description", columnDefinition = "TEXT")
    private String secondaryDescription;

    @Column(name = "vision", columnDefinition = "TEXT")
    private String vision;

    @NotNull(message = "Owner is required")
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

    @Column(name = "budget_sources", columnDefinition = "TEXT")
    private String budgetSources; // JSON array stored as string

    // Business Logic Methods

    /**
     * Get display name based on language preference
     */
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ?
                secondaryName : primaryName;
    }

    /**
     * Get display description based on language preference
     */
    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    /**
     * Calculate budget utilization percentage
     */
    public BigDecimal getBudgetUtilization() {
        if (plannedTotalBudget == null || plannedTotalBudget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (calculatedTotalPayments == null) {
            return BigDecimal.ZERO;
        }
        return calculatedTotalPayments
                .divide(plannedTotalBudget, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Check if strategy is within timeline on given date
     */
    public boolean isWithinTimeline(LocalDate date) {
        if (timelineFrom == null || timelineTo == null || date == null) {
            return false;
        }
        return !date.isBefore(timelineFrom) && !date.isAfter(timelineTo);
    }

    /**
     * Check if strategy is currently active
     */
    public boolean isCurrentlyActive() {
        return isWithinTimeline(LocalDate.now());
    }

    /**
     * Get days remaining in strategy timeline
     */
    public Long getDaysRemaining() {
        if (timelineTo == null) {
            return null;
        }
        LocalDate now = LocalDate.now();
        if (now.isAfter(timelineTo)) {
            return 0L;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(now, timelineTo);
    }
}