package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.application.dto.analytics.CumulativeSpendPoint;
import com.project.Tadafur_api.domain.strategy.entity.BudgetPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetPaymentRepository extends JpaRepository<BudgetPayment, Long> {

    /**
     * Finds all payments for a given list of entity IDs (e.g., a list of projects),
     * ordered by date. This is used to get the raw payment data for the trend analysis.
     * @param entityCode The type of entity (e.g., "PROJECT").
     * @param entityIds The list of project IDs to fetch payments for.
     * @return A list of budget payments.
     */
    List<BudgetPayment> findByEntityCodeAndEntityIdInOrderByPaymentDateAsc(String entityCode, List<Long> entityIds);

    /**
     * A powerful native SQL query that calculates the cumulative (running total) spend
     * for a list of projects directly in the database for maximum performance.
     * @param projectIds The list of project IDs to calculate trends for.
     * @return A list of data points with the cumulative spend.
     */
    @Query(value = """
        SELECT
            bp.entity_id AS "projectId",
            bp.payment_date AS "paymentDate",
            SUM(bp.amount) OVER (PARTITION BY bp.entity_id ORDER BY bp.payment_date) AS "cumulativeSpend"
        FROM
            budget_payments bp
        WHERE
            bp.entity_code = 'PROJECT' AND bp.entity_id IN (:projectIds)
        ORDER BY
            bp.entity_id, bp.payment_date ASC
        """, nativeQuery = true)
    List<CumulativeSpendPoint> findCumulativeSpendForProjects(@Param("projectIds") List<Long> projectIds);
}