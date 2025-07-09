// File: src/main/java/com/project/Tadafur_api/domain/strategy/entity/Initiative.java
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
@Table(name = "initiative")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Initiative implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id") // Links to the Program ID
    private Long parentId;

    @Column(name = "contributionpercent")
    private Double contributionPercent;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "planned_total_budget")
    private BigDecimal plannedTotalBudget;

    @Column(name = "type")
    private Integer type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "planning_status_code", length = 1024)
    private String planningStatusCode;

    @Column(name = "progress_status_code", length = 1024)
    private String progressStatusCode;

    @Type(IntArrayType.class)
    @Column(name = "vision_priorities", columnDefinition = "integer[]")
    private int[] visionPriorities;

    @Column(name = "calculated_progress_percent")
    private BigDecimal calculatedProgressPercent;

    @Column(name = "hybrid_progress_percent")
    private BigDecimal hybridProgressPercent;

    @Column(name = "calculated_total_budget")
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments")
    private BigDecimal calculatedTotalPayments;

    @Column(name = "owner_node_id")
    private Integer ownerNodeId;

    @Type(IntArrayType.class)
    @Column(name = "budget_sources", columnDefinition = "integer[]")
    private int[] budgetSources;

    @Type(JsonType.class)
    @Column(name = "name_translations", columnDefinition = "jsonb")
    private Map<String, String> nameTranslations;

    @Type(JsonType.class)
    @Column(name = "description_translations", columnDefinition = "jsonb")
    private Map<String, String> descriptionTranslations;
}