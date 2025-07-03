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
import java.util.List;

@Entity
@Table(name = "initiative")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Initiative extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "primary_name", nullable = false)
    private String primaryName;

    @Size(max = 255)
    @Column(name = "secondary_name")
    private String secondaryName;

    @Lob
    @Column(name = "primary_description")
    private String primaryDescription;

    @Lob
    @Column(name = "secondary_description")
    private String secondaryDescription;

    @NotNull
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "contributionpercent")
    private Double contributionPercent;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "planned_total_budget", precision = 15, scale = 2)
    private BigDecimal plannedTotalBudget;

    @Column(name = "type")
    private Integer type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 50)
    @Column(name = "planning_status_code")
    private String planningStatusCode;

    @Size(max = 50)
    @Column(name = "progress_status_code")
    private String progressStatusCode;

    @Column(name = "vision_priorities")
    private String visionPriorities; // JSON array as string

    @Column(name = "calculated_progress_percent", precision = 5, scale = 2)
    private BigDecimal calculatedProgressPercent;

    @Column(name = "hybrid_progress_percent", precision = 5, scale = 2)
    private BigDecimal hybridProgressPercent;

    @Column(name = "calculated_total_budget", precision = 15, scale = 2)
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments", precision = 15, scale = 2)
    private BigDecimal calculatedTotalPayments;

    @Column(name = "owner_node_id")
    private Long ownerNodeId;

    @Column(name = "budget_sources")
    private String budgetSources; // JSON array as string

    // Relationships
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects;

    // Helper methods
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
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
        if (startDate == null || endDate == null || date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isActive() {
        return "ACTIVE".equals(planningStatusCode);
    }

    public BigDecimal getEffectiveProgressPercent() {
        return hybridProgressPercent != null ? hybridProgressPercent :
                (calculatedProgressPercent != null ? calculatedProgressPercent : BigDecimal.ZERO);
    }

    public Double getEffectiveContributionPercent() {
        return contributionPercent != null ? contributionPercent : 0.0;
    }
}