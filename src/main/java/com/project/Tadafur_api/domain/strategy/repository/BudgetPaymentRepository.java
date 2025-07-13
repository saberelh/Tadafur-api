package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.BudgetPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetPaymentRepository extends JpaRepository<BudgetPayment, Long> {

    /**
     * This method correctly finds all BudgetPayment entities that match
     * a given entityId and entityCode (e.g., all payments for a specific project).
     * This is its proper home, which resolves the build error.
     */
    List<BudgetPayment> findByEntityIdAndEntityCode(Long entityId, String entityCode);
}