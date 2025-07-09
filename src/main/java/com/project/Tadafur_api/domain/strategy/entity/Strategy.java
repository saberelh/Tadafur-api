package com.project.Tadafur_api.domain.strategy.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Strategy Entity representing the 'strategy' table.
 * Contains JSONB columns for multi-language fields.
 */
@Entity
@Table(name = "strategy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Strategy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "name_translations", columnDefinition = "jsonb")
    private Map<String, String> nameTranslations;

    @Type(JsonType.class)
    @Column(name = "description_translations", columnDefinition = "jsonb")
    private Map<String, String> descriptionTranslations;

    @Column(name = "vision", columnDefinition = "TEXT")
    private String vision;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "timeline_from")
    private LocalDate timelineFrom;

    @Column(name = "timeline_to")
    private LocalDate timelineTo;

    @Column(name = "planned_total_budget")
    private BigDecimal plannedTotalBudget;

    @Column(name = "calculated_total_budget")
    private BigDecimal calculatedTotalBudget;

    @Column(name = "calculated_total_payments")
    private BigDecimal calculatedTotalPayments;

    @Column(name = "budget_sources", columnDefinition = "integer[]")
    private String budgetSources; // Keep as String for array type, or use a converter
}