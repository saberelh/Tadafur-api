// File: domain/strategy/repository/StrategyRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Strategy Repository - Data access operations for Strategy entities
 * Updated to work without status_code column
 */
@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // ================================================================
    // BASIC QUERIES for: GET /api/v1/strategies
    // ================================================================

    List<Strategy> findAllByOrderByIdDesc();

    Page<Strategy> findAllByOrderByIdDesc(Pageable pageable);

    // ================================================================
    // BY ID for: GET /api/v1/strategies/{id}
    // ================================================================

    // Just use the default findById from JpaRepository

    // ================================================================
    // BY OWNER for: GET /api/v1/strategies/by-owner/{ownerId}
    // ================================================================

    List<Strategy> findByOwnerIdOrderByIdDesc(Long ownerId);

    Page<Strategy> findByOwnerIdOrderByIdDesc(Long ownerId, Pageable pageable);

    // ================================================================
    // SEARCH for: GET /api/v1/strategies/search?query={query}
    // ================================================================

    @Query("SELECT s FROM Strategy s WHERE " +
            "(LOWER(s.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.vision) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY s.id DESC")
    Page<Strategy> searchStrategies(@Param("query") String query, Pageable pageable);

    @Query("SELECT s FROM Strategy s WHERE " +
            "(LOWER(s.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.vision) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY s.id DESC")
    List<Strategy> searchStrategiesList(@Param("query") String query);

    // ================================================================
    // ACTIVE for: GET /api/v1/strategies/active
    // ================================================================

    @Query("SELECT s FROM Strategy s WHERE " +
            "(s.timelineFrom IS NULL OR s.timelineFrom <= CURRENT_DATE) AND " +
            "(s.timelineTo IS NULL OR s.timelineTo >= CURRENT_DATE) " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findActiveStrategies();

    @Query("SELECT s FROM Strategy s WHERE " +
            "(s.timelineFrom IS NULL OR s.timelineFrom <= CURRENT_DATE) AND " +
            "(s.timelineTo IS NULL OR s.timelineTo >= CURRENT_DATE) " +
            "ORDER BY s.timelineFrom ASC")
    Page<Strategy> findActiveStrategies(Pageable pageable);

    // ================================================================
    // BY TIMELINE for: GET /api/v1/strategies/by-timeline?from={date}&to={date}
    // ================================================================

    @Query("SELECT s FROM Strategy s WHERE " +
            "((s.timelineFrom >= :fromDate AND s.timelineFrom <= :toDate) OR " +
            "(s.timelineTo >= :fromDate AND s.timelineTo <= :toDate) OR " +
            "(s.timelineFrom <= :fromDate AND s.timelineTo >= :toDate)) " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findStrategiesByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                                 @Param("toDate") LocalDate toDate);

    @Query("SELECT s FROM Strategy s WHERE " +
            "((s.timelineFrom >= :fromDate AND s.timelineFrom <= :toDate) OR " +
            "(s.timelineTo >= :fromDate AND s.timelineTo <= :toDate) OR " +
            "(s.timelineFrom <= :fromDate AND s.timelineTo >= :toDate)) " +
            "ORDER BY s.timelineFrom ASC")
    Page<Strategy> findStrategiesByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                                 @Param("toDate") LocalDate toDate,
                                                 Pageable pageable);

    // ================================================================
    // UTILITY QUERIES - For analytics and summaries
    // ================================================================

    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.ownerId = :ownerId")
    long countByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT new map(" +
            "COUNT(s) as totalCount, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(s.calculatedTotalBudget), 0) as totalCalculatedBudget, " +
            "COALESCE(SUM(s.calculatedTotalPayments), 0) as totalPayments, " +
            "COALESCE(AVG(CASE WHEN s.plannedTotalBudget > 0 THEN (s.calculatedTotalPayments / s.plannedTotalBudget * 100) ELSE 0 END), 0) as avgBudgetUtilization) " +
            "FROM Strategy s")
    java.util.Map<String, Object> getStrategySummary();

    @Query("SELECT new map(" +
            "COUNT(s) as totalCount, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(s.calculatedTotalPayments), 0) as totalPayments) " +
            "FROM Strategy s WHERE s.ownerId = :ownerId")
    java.util.Map<String, Object> getStrategySummaryByOwner(@Param("ownerId") Long ownerId);
}