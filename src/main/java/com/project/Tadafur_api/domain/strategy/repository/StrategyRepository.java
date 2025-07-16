package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.application.dto.analytics.StrategyHealthDataResult;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // This is the method that was causing the build error. It is now restored.
    List<Strategy> findByOwnerId(Long ownerId);

    @Query(
            value = "SELECT * FROM strategy s WHERE " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.name_translations) WHERE value ILIKE %:query%) OR " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.description_translations) WHERE value ILIKE %:query%) OR " +
                    "s.vision ILIKE %:query%",
            nativeQuery = true
    )
    Page<Strategy> searchStrategies(@Param("query") String query, Pageable pageable);

    @Query(
            value = "SELECT * FROM strategy s WHERE " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.name_translations) WHERE value ILIKE %:query%) OR " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.description_translations) WHERE value ILIKE %:query%) OR " +
                    "s.vision ILIKE %:query%",
            nativeQuery = true
    )
    List<Strategy> searchStrategiesList(@Param("query") String query);

    @Query(value = """
        SELECT
            s.id AS "strategyId",
            s.name_translations AS "nameTranslations",
            proj.planning_status_code AS "planningStatusCode",
            proj.progress_status_code AS "progressStatusCode",
            proj.calculated_progress_percent AS "calculatedProgressPercent",
            proj.planned_total_budget AS "plannedTotalBudget",
            proj.actual_cost AS "actualCost",
            proj.end_date AS "endDate"
        FROM
            strategy s
        LEFT JOIN
            perspective p ON s.id = p.parent_id
        LEFT JOIN
            goal g ON p.id = g.parent_id
        LEFT JOIN
            program prog ON g.id = prog.parent_id
        LEFT JOIN
            initiative i ON prog.id = i.parent_id
        LEFT JOIN
            project proj ON i.id = proj.parent_id
        WHERE
            (:strategyId IS NULL OR s.id = :strategyId)
            AND (:ownerId IS NULL OR proj.owner_id = :ownerId)
            AND proj.id IS NOT NULL
    """, nativeQuery = true)

    List<StrategyHealthDataResult> getStrategicHealthData(
            @Param("strategyId") Long strategyId,
            @Param("ownerId") Long ownerId
    );
}