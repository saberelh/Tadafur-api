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
import java.util.List;

@Entity
@Table(name = "perspective")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Perspective extends BaseEntity {

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
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotNull
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Size(max = 50)
    @Column(name = "planning_status_code")
    private String planningStatusCode;

    @Size(max = 50)
    @Column(name = "progress_status_code")
    private String progressStatusCode;

    @Column(name = "calculated_total_budget", precision = 15, scale = 2)
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments", precision = 15, scale = 2)
    private BigDecimal calculatedTotalPayments;

    @Column(name = "planned_total_budget", precision = 15, scale = 2)
    private BigDecimal plannedTotalBudget;

    @Column(name = "budget_sources")
    private String budgetSources; // JSON array as string

    // Relationships
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Goal> goals;

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

    public boolean isActive() {
        return "ACTIVE".equals(planningStatusCode);
    }
}