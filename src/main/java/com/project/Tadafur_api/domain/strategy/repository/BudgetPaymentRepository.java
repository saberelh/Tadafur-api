package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.BudgetPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetPaymentRepository extends JpaRepository<BudgetPayment, Long> {
    List<BudgetPayment> findByEntityIdAndEntityCode(Long entityId, String entityCode);
}