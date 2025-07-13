package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByParentId(Long parentId);

    List<Project> findByOwnerId(Long ownerId);

    /**
     * This method calculates the sum of all payments for a single project.
     * It correctly queries the BudgetPayment entity using JPQL.
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM BudgetPayment p WHERE p.entityId = :projectId AND p.entityCode = 'PROJECT'")
    BigDecimal getSumOfPaymentsForProject(@Param("projectId") Long projectId);
}