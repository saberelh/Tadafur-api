package com.project.Tadafur_api.domain.strategy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "strategy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "arabic_name")
    private String arabicName;

    @Column(name = "english_name")
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
    private String budgetSources; // JSON array as string

    // Audit fields
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

    // Helper methods for dashboards
    public boolean isActive() {
        return "ACTIVE".equals(statusCode);
    }

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

    public String getTimelineStatus() {
        LocalDate now = LocalDate.now();
        if (timelineFrom == null || timelineTo == null) {
            return "UNDEFINED";
        }
        if (now.isBefore(timelineFrom)) {
            return "UPCOMING";
        } else if (now.isAfter(timelineTo)) {
            return "COMPLETED";
        } else {
            return "ACTIVE";
        }
    }
}