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

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // Basic queries
    List<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Strategy> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Strategy> findByIdAndStatusCode(Long id, String statusCode);

    // Owner-based queries
    List<Strategy> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    Page<Strategy> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode, Pageable pageable);

    // Search queries
    @Query("SELECT s FROM Strategy s WHERE " +
            "(LOWER(s.arabicName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.englishName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.arabicDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.englishDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.createdAt DESC")
    Page<Strategy> searchStrategies(@Param("query") String query,
                                    @Param("statusCode") String statusCode,
                                    Pageable pageable);

    // Timeline-based queries
    @Query("SELECT s FROM Strategy s WHERE " +
            "s.timelineFrom <= :date AND s.timelineTo >= :date AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findActiveStrategiesOnDate(@Param("date") LocalDate date,
                                              @Param("statusCode") String statusCode);

    @Query("SELECT s FROM Strategy s WHERE " +
            "s.timelineFrom BETWEEN :fromDate AND :toDate AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findStrategiesByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                                 @Param("toDate") LocalDate toDate,
                                                 @Param("statusCode") String statusCode);

    // Budget-based queries
    @Query("SELECT s FROM Strategy s WHERE " +
            "s.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.plannedTotalBudget DESC")
    List<Strategy> findStrategiesByBudgetRange(@Param("minBudget") java.math.BigDecimal minBudget,
                                               @Param("maxBudget") java.math.BigDecimal maxBudget,
                                               @Param("statusCode") String statusCode);

    // Year-based queries
    @Query("SELECT s FROM Strategy s WHERE " +
            "YEAR(s.timelineFrom) = :year AND " +
            "s.statusCode = :statusCode " +
            "ORDER BY s.timelineFrom ASC")
    List<Strategy> findStrategiesByYear(@Param("year") int year,
                                        @Param("statusCode") String statusCode);

    // Recent strategies
    @Query(value = "SELECT * FROM strategy s WHERE s.status_code = :statusCode " +
            "ORDER BY s.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Strategy> findRecentStrategies(@Param("statusCode") String statusCode,
                                        @Param("limit") int limit);

    // Count queries for analytics
    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.statusCode = :statusCode")
    long countByStatusCode(@Param("statusCode") String statusCode);

    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.ownerId = :ownerId AND s.statusCode = :statusCode")
    long countByOwnerIdAndStatusCode(@Param("ownerId") Long ownerId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(s) as totalCount, " +
            "COALESCE(SUM(s.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(s.calculatedTotalBudget), 0) as totalCalculatedBudget, " +
            "COALESCE(SUM(s.calculatedTotalPayments), 0) as totalPayments) " +
            "FROM Strategy s WHERE s.statusCode = :statusCode")
    java.util.Map<String, Object> getStrategySummary(@Param("statusCode") String statusCode);
}