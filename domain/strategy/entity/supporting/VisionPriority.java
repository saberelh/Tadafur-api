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
@Table(name = "vision_priorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisionPriority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "primary_name", nullable = false)
    private String primaryName;

    @Size(max = 500)
    @Column(name = "secondary_name")
    private String secondaryName;

    @Column(name = "pillar_id")
    private Long pillarId;

    @Lob
    @Column(name = "primary_strategic_objective")
    private String primaryStrategicObjective;

    @Lob
    @Column(name = "secondary_strategic_objective")
    private String secondaryStrategicObjective;

    @Lob
    @Column(name = "primary_description")
    private String primaryDescription;

    @Lob
    @Column(name = "secondary_description")
    private String secondaryDescription;

    // Helper methods
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    public String getDisplayStrategicObjective() {
        return secondaryStrategicObjective != null && !secondaryStrategicObjective.trim().isEmpty() ?
                secondaryStrategicObjective : primaryStrategicObjective;
    }
}