// File: src/main/java/com/project/Tadafur_api/domain/strategy/entity/ProjectWorkItem.java
package com.project.Tadafur_api.domain.strategy.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "project_work_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectWorkItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id") // Links to the Project ID
    private Long projectId;

    @Column(name = "parent_id") // For sub-tasks
    private Long parentId;

    @Column(name = "priority_id")
    private Integer priorityId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "assignee_user_id")
    private Integer assigneeUserId;

    @Column(name = "estimated_time")
    private BigDecimal estimatedTime;

    @Column(name = "estimated_time_unit")
    private Integer estimatedTimeUnit;

    @Column(name = "actual_time")
    private BigDecimal actualTime;

    @Column(name = "actual_time_unit")
    private Integer actualTimeUnit;

    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name = "planned_due_date")
    private LocalDate plannedDueDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_due_date")
    private LocalDate actualDueDate;

    @Column(name = "progress_percent")
    private BigDecimal progressPercent;

    @Column(name = "work_item_group_id")
    private Long workItemGroupId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "item_sort")
    private Integer itemSort;

    @Column(name = "verification_result", length = 2048)
    private String verificationResult;

    @Column(name = "progress_by_effort")
    private BigDecimal progressByEffort;

    @Column(name = "progress_by_average")
    private BigDecimal progressByAverage;

    @Column(name = "manual_progress_by_effort")
    private BigDecimal manualProgressByEffort;

    @Column(name = "manual_progress_by_average")
    private BigDecimal manualProgressByAverage;

    @Column(name = "is_added_from_custom")
    private Boolean isAddedFromCustom;

    @Type(JsonType.class)
    @Column(name = "name_translations", columnDefinition = "jsonb")
    private Map<String, String> nameTranslations;

    @Type(JsonType.class)
    @Column(name = "description_translations", columnDefinition = "jsonb")
    private Map<String, String> descriptionTranslations;
}