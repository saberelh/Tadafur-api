package com.project.Tadafur_api.domain.strategy.entity.supporting;

import com.project.Tadafur_api.shared.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "progress_point")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProgressPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "percent", precision = 5, scale = 2)
    private Double percent;

    // Helper methods
    public boolean isValidProgress() {
        return percent != null && percent >= 0.0 && percent <= 100.0;
    }

    public String getFormattedPercent() {
        return percent != null ? String.format("%.2f%%", percent) : "0.00%";
    }
}