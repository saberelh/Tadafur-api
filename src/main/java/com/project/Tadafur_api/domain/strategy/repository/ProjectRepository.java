package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Finds all projects that are direct children of a given initiative.
     * This is part of the original application's hierarchy logic.
     */
    List<Project> findByParentId(Long parentId);

    /**
     * Finds all projects assigned to a specific owner. This is used by the
     * analytics service to filter the projects before processing.
     */
    List<Project> findByOwnerId(Long ownerId);

    /**
     * A projection interface to map the results from our efficient native SQL query.
     * This helps Spring Data JPA correctly handle the custom columns returned by the query.
     */
    public interface ProjectPaymentDetails {
        Long getProjectId();
        String getNameTranslations();
        java.math.BigDecimal getPlannedTotalBudget();
        Long getPaymentId();
        java.time.LocalDate getPaymentDate();
        java.math.BigDecimal getPaymentAmount();
        String getNotesTranslations();
        String getPaymentCreatedBy();
        java.time.LocalDateTime getPaymentCreatedAt();
    }

    /**
     * The single, efficient query to get all project and payment data at once.
     * This query joins the project and budget_payments tables and returns a flat
     * list of results, which is much faster than making multiple database calls.
     * It handles all filtering scenarios (by projectId, by ownerId, or all) at the database level.
     */
    @Query(value = """
        SELECT
            p.id AS "projectId",
            p.name_translations AS "nameTranslations",
            p.planned_total_budget AS "plannedTotalBudget",
            bp.id AS "paymentId",
            bp.payment_date AS "paymentDate",
            bp.amount AS "paymentAmount",
            bp.note_translations AS "notesTranslations",
            bp.created_by AS "paymentCreatedBy",
            bp.created_at AS "paymentCreatedAt"
        FROM
            project p
        LEFT JOIN
            budget_payments bp ON p.id = bp.entity_id AND bp.entity_code = 'PROJECT'
        WHERE
            (:projectId IS NULL OR p.id = :projectId) AND
            (:ownerId IS NULL OR p.owner_id = :ownerId)
        ORDER BY
            p.id, bp.payment_date
    """, nativeQuery = true)
    List<ProjectPaymentDetails> getProjectPaymentDetails(
            @Param("projectId") Long projectId,
            @Param("ownerId") Long ownerId
    );
}