package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // ===== BASIC READ OPERATIONS =====

    List<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Strategy> findByIdAndStatusCode(Long id, String statusCode);

    // ===== DASHBOARD QUERIES =====

    // Get strategies by owner (for authority-specific dashboards)
    @Query("SELECT s FROM Strategy s WHERE s.ownerId = :ownerId AND s.statusCode = 'ACTIVE' ORDER BY s.createdAt DESC")
    List<Strategy> findByOwnerId(@Param("ownerId") Long ownerId);

    Page<Strategy> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode, Pageable pageable);

    // Search for dashboard filtering
    @Query("SELECT s FROM Strategy s WHERE " +
            "(LOWER(s.arabicName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.englishName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.arabicDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.englishDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "s.statusCode = 'ACTIVE' " +
            "ORDER BY s.createdAt DESC")
    Page<Strategy> searchStrategies(@Param("query") String query, Pageable pageable);

    // ===== TIMELINE ANALYTICS =====

    // Active strategies on specific date (for timeline dashboard)
    @Query("SELECT s FROM Strategy s WHERE " +
            "s.timelineFrom <= :date AND s.timelineTo >= :date AND " +
            "s.statusCode = 'ACTIVE' " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findActiveStrategiesOnDate(@Param("date") LocalDate date);

    // Strategies by timeline range (for planning dashboard)
    @Query("SELECT s FROM Strategy s WHERE " +
            "s.timelineFrom BETWEEN :fromDate AND :toDate AND " +
            "s.statusCode = 'ACTIVE' " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findStrategiesByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                                 @Param("toDate") LocalDate toDate);

    // Strategies by year (for annual planning)
    @Query("SELECT s FROM Strategy s WHERE " +
            "YEAR(s.timelineFrom) = :year AND " +
            "s.statusCode = 'ACTIVE' " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findStrategiesByYear(@Param("year") int year);

    // ===== FINANCIAL ANALYTICS =====

    // Strategies by budget range (for financial dashboard)
    @Query("SELECT s FROM Strategy s WHERE " +
            "s.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "s.statusCode = 'ACTIVE' " +
            "ORDER BY s.plannedTotalBudget DESC")
    List<Strategy> findStrategiesByBudgetRange(@Param("minBudget") BigDecimal minBudget,
                                               @Param("maxBudget") BigDecimal maxBudget);

    // Top strategies by budget (for executive dashboard)
    @Query(value = "SELECT * FROM strategy s WHERE s.status_code = 'ACTIVE' " +
            "ORDER BY s.planned_total_budget DESC LIMIT :limit", nativeQuery = true)
    List<Strategy> findTopStrategiesByBudget(@Param("limit") int limit);

    // Recent strategies (for dashboard activity feed)
    @Query(value = "SELECT * FROM strategy s WHERE s.status_code = 'ACTIVE' " +
            "ORDER BY s.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Strategy> findRecentStrategies(@Param("limit") int limit);

    // ===== DASHBOARD ANALYTICS QUERIES =====

    // Strategy summary for executive dashboard
    @Query("SELECT new map(" +
            "COUNT(s) as totalStrategies, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(s.calculatedTotalBudget), 0) as totalCalculatedBudget, " +
            "COALESCE(SUM(s.calculatedTotalPayments), 0) as totalPayments, " +
            "COALESCE(AVG(s.plannedTotalBudget), 0) as averageBudget) " +
            "FROM Strategy s WHERE s.statusCode = 'ACTIVE'")
    Map<String, Object> getStrategySummary();

    // Budget utilization summary
    @Query("SELECT new map(" +
            "COUNT(s) as totalCount, " +
            "SUM(CASE WHEN s.calculatedTotalPayments > s.plannedTotalBudget THEN 1 ELSE 0 END) as overBudgetCount, " +
            "SUM(CASE WHEN s.calculatedTotalPayments < s.plannedTotalBudget * 0.8 THEN 1 ELSE 0 END) as underUtilizedCount, " +
            "AVG(CASE WHEN s.plannedTotalBudget > 0 THEN (s.calculatedTotalPayments * 100.0 / s.plannedTotalBudget) ELSE 0 END) as averageUtilization) " +
            "FROM Strategy s WHERE s.statusCode = 'ACTIVE' AND s.plannedTotalBudget IS NOT NULL")
    Map<String, Object> getBudgetUtilizationSummary();

    // Timeline status distribution
    @Query("SELECT new map(" +
            "SUM(CASE WHEN s.timelineFrom > CURRENT_DATE THEN 1 ELSE 0 END) as upcomingCount, " +
            "SUM(CASE WHEN s.timelineFrom <= CURRENT_DATE AND s.timelineTo >= CURRENT_DATE THEN 1 ELSE 0 END) as activeCount, " +
            "SUM(CASE WHEN s.timelineTo < CURRENT_DATE THEN 1 ELSE 0 END) as completedCount) " +
            "FROM Strategy s WHERE s.statusCode = 'ACTIVE' AND s.timelineFrom IS NOT NULL AND s.timelineTo IS NOT NULL")
    Map<String, Object> getTimelineStatusDistribution();

    // Monthly strategy creation trend (for activity dashboard)
    @Query(value = "SELECT " +
            "DATE_TRUNC('month', s.created_at) as month, " +
            "COUNT(*) as count " +
            "FROM strategy s " +
            "WHERE s.status_code = 'ACTIVE' " +
            "AND s.created_at >= CURRENT_DATE - INTERVAL '12 months' " +
            "GROUP BY DATE_TRUNC('month', s.created_at) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyCreationTrend();

    // Authority participation (for collaboration dashboard)
    @Query("SELECT new map(" +
            "s.ownerId as authorityId, " +
            "COUNT(s) as strategyCount, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalBudget) " +
            "FROM Strategy s " +
            "WHERE s.statusCode = 'ACTIVE' " +
            "GROUP BY s.ownerId " +
            "ORDER BY COUNT(s) DESC")
    List<Map<String, Object>> getAuthorityParticipation();

    // ===== COUNT QUERIES =====

    long countByStatusCode(String statusCode);

    long countByOwnerIdAndStatusCode(Long ownerId, String statusCode);

    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.statusCode = 'ACTIVE' AND s.timelineFrom <= CURRENT_DATE AND s.timelineTo >= CURRENT_DATE")
    long countActiveStrategies();

    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.statusCode = 'ACTIVE' AND s.plannedTotalBudget > 0")
    long countStrategiesWithBudget();
}