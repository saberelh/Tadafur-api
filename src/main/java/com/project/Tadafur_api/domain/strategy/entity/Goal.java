// File: src/main/java/com/project/Tadafur_api/domain/strategy/entity/Goal.java
package com.project.Tadafur_api.domain.strategy.entity;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id") // Links to the Perspective ID
    private Long parentId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "planning_status_code", length = 1024)
    private String planningStatusCode;

    @Column(name = "progress_status_code", length = 1024)
    private String progressStatusCode;

    @Column(name = "calculated_progress_percent")
    private BigDecimal calculatedProgressPercent;

    @Column(name = "hybrid_progress_percent")
    private BigDecimal hybridProgressPercent;

    @Column(name = "vision_priority")
    private Integer visionPriority;

    @Column(name = "planned_total_budget")
    private BigDecimal plannedTotalBudget;

    @Column(name = "calculated_total_budget")
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments")
    private BigDecimal calculatedTotalPayments;

    @Type(IntArrayType.class)
    @Column(name = "budget_sources", columnDefinition = "integer[]")
    private int[] budgetSources;

    @Column(name = "owner_node_id")
    private Integer ownerNodeId;

    @Type(JsonType.class)
    @Column(name = "name_translations", columnDefinition = "jsonb")
    private Map<String, String> nameTranslations;

    @Type(JsonType.class)
    @Column(name = "description_translations", columnDefinition = "jsonb")
    private Map<String, String> descriptionTranslations;
}