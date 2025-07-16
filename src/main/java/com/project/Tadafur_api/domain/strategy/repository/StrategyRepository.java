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
    public interface FlatHierarchyResult {
        Long getStrategyId();
        String getStrategyName();
        Long getPerspectiveId();
        String getPerspectiveName();
        Long getGoalId();
        String getGoalName();
        Long getProgramId();
        String getProgramName();
        Long getInitiativeId();
        String getInitiativeName();
        Long getProjectId();
        String getProjectName();
    }
    @Query(value = """
        SELECT
            s.id AS "strategyId",
            s.name_translations AS "strategyName",
            p.id AS "perspectiveId",
            p.name_translations AS "perspectiveName",
            g.id AS "goalId",
            g.name_translations AS "goalName",
            prog.id AS "programId",
            prog.name_translations AS "programName",
            i.id AS "initiativeId",
            i.name_translations AS "initiativeName",
            proj.id AS "projectId",
            proj.name_translations AS "projectName"
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
            AND (
                -- This subquery correctly filters the results for a specific owner
                :ownerId IS NULL OR proj.id IN (
                    SELECT p_filter.id FROM project p_filter WHERE p_filter.owner_id = :ownerId
                )
            )
        ORDER BY
            s.id, p.id, g.id, prog.id, i.id, proj.id
    """, nativeQuery = true)
    List<FlatHierarchyResult> getFlatHierarchy(
            @Param("strategyId") Long strategyId,
            @Param("ownerId") Long ownerId
    );

}