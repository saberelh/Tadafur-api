// File: src/main/java/com/project/Tadafur_api/domain/strategy/entity/Perspective.java
package com.project.Tadafur_api.domain.strategy.entity;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "perspective")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perspective implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "parent_id") // Links to the Strategy ID
    private Long parentId;

    @Column(name = "planning_status_code", length = 1024)
    private String planningStatusCode;

    @Column(name = "progress_status_code", length = 1024)
    private String progressStatusCode;

    @Column(name = "calculated_total_budget")
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments")
    private BigDecimal calculatedTotalPayments;

    @Column(name = "planned_total_budget")
    private BigDecimal plannedTotalBudget;

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