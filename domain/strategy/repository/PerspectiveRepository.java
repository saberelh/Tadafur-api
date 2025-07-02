package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerspectiveRepository extends JpaRepository<Perspective, Long> {

    // Basic queries
    List<Perspective> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Perspective> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Perspective> findByIdAndStatusCode(Long id, String statusCode);

    // Parent-based queries
    List<Perspective> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode);

    Page<Perspective> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode, Pageable pageable);

    // Owner-based queries
    List<Perspective> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries - Updated for new field names
    @Query("SELECT p FROM Perspective p WHERE " +
            "(LOWER(p.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.createdAt DESC")
    Page<Perspective> searchPerspectives(@Param("query") String query,
                                         @Param("statusCode") String statusCode,
                                         Pageable pageable);

    // Status-based queries
    @Query("SELECT p FROM Perspective p WHERE " +
            "p.planningStatusCode = :planningStatus AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.createdAt DESC")
    List<Perspective> findByPlanningStatus(@Param("planningStatus") String planningStatus,
                                           @Param("statusCode") String statusCode);

    // Budget-based queries
    @Query("SELECT p FROM Perspective p WHERE " +
            "p.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.plannedTotalBudget DESC")
    List<Perspective> findPerspectivesByBudgetRange(@Param("minBudget") java.math.BigDecimal minBudget,
                                                    @Param("maxBudget") java.math.BigDecimal maxBudget,
                                                    @Param("statusCode") String statusCode);

    // Count queries
    @Query("SELECT COUNT(p) FROM Perspective p WHERE p.parentId = :strategyId AND p.statusCode = :statusCode")
    long countByStrategyId(@Param("strategyId") Long strategyId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(p) FROM Perspective p WHERE p.ownerId = :ownerId AND p.statusCode = :statusCode")
    long countByOwnerIdAndStatusCode(@Param("ownerId") Long ownerId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(p) as totalCount, " +
            "COALESCE(SUM(p.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(p.calculatedTotalBudget), 0) as totalCalculatedBudget, " +
            "COALESCE(SUM(p.calculatedTotalPayments), 0) as totalPayments) " +
            "FROM Perspective p WHERE p.parentId = :strategyId AND p.statusCode = :statusCode")
    java.util.Map<String, Object> getPerspectiveSummaryByStrategy(@Param("strategyId") Long strategyId,
                                                                  @Param("statusCode") String statusCode);
}