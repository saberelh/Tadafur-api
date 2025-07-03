package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // Basic queries
    List<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Strategy> findByIdAndStatusCode(Long id, String statusCode);

    // Owner-based queries
    List<Strategy> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries
    @Query("SELECT s FROM Strategy s WHERE " +
            "(LOWER(s.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.createdAt DESC")
    Page<Strategy> searchStrategies(@Param("query") String query,
                                    @Param("statusCode") String statusCode,
                                    Pageable pageable);

    // Summary queries
    @Query("SELECT new map(COUNT(s) as totalCount, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(s.calculatedTotalBudget), 0) as totalCalculatedBudget, " +
            "COALESCE(SUM(s.calculatedTotalPayments), 0) as totalPayments) " +
            "FROM Strategy s WHERE s.statusCode = :statusCode")
    java.util.Map<String, Object> getStrategySummary(@Param("statusCode") String statusCode);
}