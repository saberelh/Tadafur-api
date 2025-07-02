package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    // Basic queries
    List<Program> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Program> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Program> findByIdAndStatusCode(Long id, String statusCode);

    // Parent-based queries
    List<Program> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode);

    Page<Program> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode, Pageable pageable);

    // Owner-based queries
    List<Program> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries - Updated for new field names
    @Query("SELECT p FROM Program p WHERE " +
            "(LOWER(p.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.createdAt DESC")
    Page<Program> searchPrograms(@Param("query") String query,
                                 @Param("statusCode") String statusCode,
                                 Pageable pageable);

    // Progress-based queries
    @Query("SELECT p FROM Program p WHERE " +
            "p.calculatedProgressPercent >= :minProgress AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.calculatedProgressPercent DESC")
    List<Program> findByMinimumProgress(@Param("minProgress") java.math.BigDecimal minProgress,
                                        @Param("statusCode") String statusCode);

    // Vision alignment queries
    @Query("SELECT p FROM Program p WHERE " +
            "p.visionPriorities LIKE CONCAT('%', :visionPriorityId, '%') AND " +
            "p.statusCode = :statusCode")
    List<Program> findByVisionPriority(@Param("visionPriorityId") String visionPriorityId,
                                       @Param("statusCode") String statusCode);

    // Contribution-based queries
    @Query("SELECT p FROM Program p WHERE " +
            "p.contributionPercent >= :minContribution AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.contributionPercent DESC")
    List<Program> findByMinimumContribution(@Param("minContribution") Double minContribution,
                                            @Param("statusCode") String statusCode);

    // Budget-based queries
    @Query("SELECT p FROM Program p WHERE " +
            "p.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.plannedTotalBudget DESC")
    List<Program> findProgramsByBudgetRange(@Param("minBudget") java.math.BigDecimal minBudget,
                                            @Param("maxBudget") java.math.BigDecimal maxBudget,
                                            @Param("statusCode") String statusCode);

    // Status-based queries
    @Query("SELECT p FROM Program p WHERE " +
            "p.planningStatusCode = :planningStatus AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.createdAt DESC")
    List<Program> findByPlanningStatus(@Param("planningStatus") String planningStatus,
                                       @Param("statusCode") String statusCode);

    // Count queries
    @Query("SELECT COUNT(p) FROM Program p WHERE p.parentId = :goalId AND p.statusCode = :statusCode")
    long countByGoalId(@Param("goalId") Long goalId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(p) FROM Program p WHERE p.ownerId = :ownerId AND p.statusCode = :statusCode")
    long countByOwnerIdAndStatusCode(@Param("ownerId") Long ownerId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(p) as totalCount, " +
            "COALESCE(AVG(p.calculatedProgressPercent), 0) as averageProgress, " +
            "COALESCE(SUM(p.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(p.calculatedTotalPayments), 0) as totalPayments, " +
            "COALESCE(AVG(p.contributionPercent), 0) as averageContribution) " +
            "FROM Program p WHERE p.parentId = :goalId AND p.statusCode = :statusCode")
    java.util.Map<String, Object> getProgramSummaryByGoal(@Param("goalId") Long goalId,
                                                          @Param("statusCode") String statusCode);
}