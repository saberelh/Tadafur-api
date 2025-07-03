package com.project.Tadafur_api.domain.strategy.entity.supporting;

import com.project.Tadafur_api.shared.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "annual_plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AnnualPlan extends BaseEntity {

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
    @Column(name = "plan_year", nullable = false)
    private Integer planYear;

    @Size(max = 50)
    @Column(name = "plan_status")
    private String planStatus;

    // Helper methods
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    public boolean isActive() {
        return "ACTIVE".equals(planStatus);
    }

    public boolean isCurrentYear() {
        return planYear != null && planYear.equals(java.time.Year.now().getValue());
    }
}