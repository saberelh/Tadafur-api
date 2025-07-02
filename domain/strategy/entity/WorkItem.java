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
@Table(name = "project_work_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "parent_id")
    private Long parentId;

    @NotNull
    @Size(max = 255)
    @Column(name = "secondary_name")
    private String secondaryName;

    @Lob
    @Column(name = "primary_description")
    private String primaryDescription;

    @Lob
    @Column(name = "secondary_description")
    private String secondaryDescription;

    @Column(name = "priority_id")
    private Long priorityId;

    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "assignee_user_id")
    private Long assigneeUserId;

    @Column(name = "estimated_time", precision = 8, scale = 2)
    private BigDecimal estimatedTime;

    @Column(name = "estimated_time_unit")
    private Integer estimatedTimeUnit;

    @Column(name = "actual_time", precision = 8, scale = 2)
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

    @Column(name = "progress_percent", precision = 5, scale = 2)
    private BigDecimal progressPercent;

    @Column(name = "work_item_group_id")
    private Long workItemGroupId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "item_sort")
    private Integer itemSort;

    @Size(max = 500)
    @Column(name = "verification_result")
    private String verificationResult;

    @Column(name = "progress_by_effort", precision = 5, scale = 2)
    private BigDecimal progressByEffort;

    @Column(name = "progress_by_average", precision = 5, scale = 2)
    private BigDecimal progressByAverage;

    @Column(name = "manual_progress_by_effort", precision = 5, scale = 2)
    private BigDecimal manualProgressByEffort;

    @Column(name = "manual_progress_by_average", precision = 5, scale = 2)
    private BigDecimal manualProgressByAverage;

    @Column(name = "is_added_from_custom")
    private Boolean isAddedFromCustom;

    // Relationships
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkItem> subTasks;

    // Helper methods
    public String getDisplayName() {
        return secondaryName != null && !secondaryName.trim().isEmpty() ? secondaryName : primaryName;
    }

    public String getDisplayDescription() {
        return secondaryDescription != null && !secondaryDescription.trim().isEmpty() ?
                secondaryDescription : primaryDescription;
    }

    public boolean isCompleted() {
        return progressPercent != null && progressPercent.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    public boolean isOverdue() {
        if (plannedDueDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(plannedDueDate) && !isCompleted();
    }

    public BigDecimal getEffectiveProgress() {
        if (manualProgressByEffort != null) {
            return manualProgressByEffort;
        }
        if (progressByEffort != null) {
            return progressByEffort;
        }
        return progressPercent != null ? progressPercent : BigDecimal.ZERO;
    }
}(max = 255)
@Column(name = "primary_name", nullable = false)
private String primaryName;

@Size