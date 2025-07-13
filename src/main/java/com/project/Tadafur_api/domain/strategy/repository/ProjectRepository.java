// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/ProjectRepository.java
package com.project.Tadafur_api.domain.strategy.repository;


import com.project.Tadafur_api.application.dto.analytics.CumulativeSpendPoint;
import com.project.Tadafur_api.domain.strategy.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // This method is unchanged: It finds all projects for a specific initiative.
    List<Project> findByParentId(Long parentId);

    @Query(value = """
    SELECT p.* FROM project p
    JOIN initiative i ON p.parent_id = i.id
    JOIN program prog ON i.parent_id = prog.id
    JOIN goal g ON prog.parent_id = g.id
    JOIN perspective persp ON g.parent_id = persp.id
    WHERE persp.parent_id = :strategyId
    """, nativeQuery = true)
    List<Project> findAllDescendantProjectsByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * NEW METHOD: Finds all projects owned by a specific authority.
     */


    @Query(value = """
    SELECT
        p.id AS "projectId",
        bp.payment_date AS "paymentDate",
        SUM(bp.amount) OVER (PARTITION BY p.id ORDER BY bp.payment_date) AS "cumulativeSpend"
    FROM
        project p
    JOIN
        budget_payments bp ON p.id = bp.entity_id AND bp.entity_code = 'PROJECT'
    WHERE
        p.id IN (:projectIds)
    ORDER BY
        p.id, bp.payment_date ASC
    """, nativeQuery = true)
    List<CumulativeSpendPoint> findCumulativeSpendForProjects(@Param("projectIds") List<Long> projectIds);
    List<Project> findByOwnerId(Long ownerId);
}
