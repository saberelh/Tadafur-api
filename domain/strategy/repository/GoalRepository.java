package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Goal;
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
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // Basic queries
    List<Goal> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Goal> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Goal> findByIdAndStatusCode(Long id, String statusCode);

    // Parent-based queries
    List<Goal> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode);

    Page<Goal> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode, Pageable pageable);

    // Owner-based queries
    List<Goal> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries - Updated for new field names
    @Query("SELECT g FROM Goal g WHERE " +
            "(LOWER(g.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "g.statusCode = :statusCode " +
            "ORDER BY g.createdAt DESC")
    Page<Goal> searchGoals(@Param("query") String query,
                           @Param("statusCode") String statusCode,
                           Pageable pageable);

    // Timeline-based queries
    @Query("SELECT g FROM Goal g WHERE " +
            "g.startDate <= :date AND (g.endDate IS NULL OR g.endDate >= :date) AND " +
            "g.statusCode = :statusCode " +
            "ORDER BY g.startDate ASC")
    List<Goal> findActiveGoalsOnDate(@Param("date") LocalDate date,
                                     @Param("statusCode") String statusCode);

    @Query("SELECT g FROM Goal g WHERE " +
            "g.startDate BETWEEN :fromDate AND :toDate AND " +
            "g.statusCode = :statusCode " +
            "ORDER BY g.startDate ASC")
    List<Goal> findGoalsByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("statusCode") String statusCode);

    // Vision priority queries
    List<Goal> findByVisionPriorityAndStatusCodeOrderByCreatedAtDesc(Long visionPriority, String statusCode);

    @Query("SELECT g FROM Goal g WHERE g.visionPriority = :visionPriority AND g.statusCode = :statusCode")
    List<Goal> findByVisionPriority(@Param("visionPriority") Long visionPriority,
                                    @Param("statusCode") String statusCode);

    // Progress-based queries
    @Query("SELECT g FROM Goal g WHERE " +
            "g.calculatedProgressPercent >= :minProgress AND " +
            "g.statusCode = :statusCode " +
            "ORDER BY g.calculatedProgressPercent DESC")
    List<Goal> findByMinimumProgress(@Param("minProgress") java.math.BigDecimal minProgress,
                                     @Param("statusCode") String statusCode);

    // Budget-based queries
    @Query("SELECT g FROM Goal g WHERE " +
            "g.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "g.statusCode = :statusCode " +
            "ORDER BY g.plannedTotalBudget DESC")
    List<Goal> findGoalsByBudgetRange(@Param("minBudget") java.math.BigDecimal minBudget,
                                      @Param("maxBudget") java.math.BigDecimal maxBudget,
                                      @Param("statusCode") String statusCode);

    // Count queries
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.parentId = :perspectiveId AND g.statusCode = :statusCode")
    long countByPerspectiveId(@Param("perspectiveId") Long perspectiveId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.visionPriority = :visionPriority AND g.statusCode = :statusCode")
    long countByVisionPriority(@Param("visionPriority") Long visionPriority, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(g) as totalCount, " +
            "COALESCE(AVG(g.calculatedProgressPercent), 0) as averageProgress, " +
            "COALESCE(SUM(g.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(g.calculatedTotalPayments), 0) as totalPayments) " +
            "FROM Goal g WHERE g.parentId = :perspectiveId AND g.statusCode = :statusCode")
    java.util.Map<String, Object> getGoalSummaryByPerspective(@Param("perspectiveId") Long perspectiveId,
                                                              @Param("statusCode") String statusCode);
}