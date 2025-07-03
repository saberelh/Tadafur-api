package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Initiative;
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
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    // Basic queries
    List<Initiative> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Initiative> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Initiative> findByIdAndStatusCode(Long id, String statusCode);

    // Parent-based queries
    List<Initiative> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode);

    Page<Initiative> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode, Pageable pageable);

    // Owner-based queries
    List<Initiative> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries - Updated for new field names
    @Query("SELECT i FROM Initiative i WHERE " +
            "(LOWER(i.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "i.statusCode = :statusCode " +
            "ORDER BY i.createdAt DESC")
    Page<Initiative> searchInitiatives(@Param("query") String query,
                                       @Param("statusCode") String statusCode,
                                       Pageable pageable);

    // Progress-based queries
    @Query("SELECT i FROM Initiative i WHERE " +
            "i.calculatedProgressPercent >= :minProgress AND " +
            "i.statusCode = :statusCode " +
            "ORDER BY i.calculatedProgressPercent DESC")
    List<Initiative> findByMinimumProgress(@Param("minProgress") java.math.BigDecimal minProgress,
                                           @Param("statusCode") String statusCode);

    // Vision alignment queries
    @Query("SELECT i FROM Initiative i WHERE " +
            "i.visionPriorities LIKE CONCAT('%', :visionPriorityId, '%') AND " +
            "i.statusCode = :statusCode")
    List<Initiative> findByVisionPriority(@Param("visionPriorityId") String visionPriorityId,
                                          @Param("statusCode") String statusCode);

    // Owner node queries
    List<Initiative> findByOwnerNodeIdAndStatusCode(Long ownerNodeId, String statusCode);

    // Timeline queries
    @Query("SELECT i FROM Initiative i WHERE " +
            "i.startDate <= :date AND (i.endDate IS NULL OR i.endDate >= :date) AND " +
            "i.statusCode = :statusCode")
    List<Initiative> findActiveInitiativesOnDate(@Param("date") LocalDate date,
                                                 @Param("statusCode") String statusCode);

    @Query("SELECT i FROM Initiative i WHERE " +
            "i.startDate BETWEEN :fromDate AND :toDate AND " +
            "i.statusCode = :statusCode " +
            "ORDER BY i.startDate ASC")
    List<Initiative> findInitiativesByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                                    @Param("toDate") LocalDate toDate,
                                                    @Param("statusCode") String statusCode);

    // Budget utilization queries
    @Query("SELECT i FROM Initiative i WHERE " +
            "i.plannedTotalBudget > 0 AND " +
            "(i.calculatedTotalPayments / i.plannedTotalBudget) >= :utilizationThreshold AND " +
            "i.statusCode = :statusCode")
    List<Initiative> findByBudgetUtilization(@Param("utilizationThreshold") java.math.BigDecimal utilizationThreshold,
                                             @Param("statusCode") String statusCode);

    // Type-based queries
    List<Initiative> findByTypeAndStatusCodeOrderByCreatedAtDesc(Integer type, String statusCode);

    // Count queries
    @Query("SELECT COUNT(i) FROM Initiative i WHERE i.parentId = :programId AND i.statusCode = :statusCode")
    long countByProgramId(@Param("programId") Long programId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(i) FROM Initiative i WHERE i.ownerNodeId = :ownerNodeId AND i.statusCode = :statusCode")
    long countByOwnerNodeId(@Param("ownerNodeId") Long ownerNodeId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(i) as totalCount, " +
            "COALESCE(AVG(i.calculatedProgressPercent), 0) as averageProgress, " +
            "COALESCE(SUM(i.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(i.calculatedTotalPayments), 0) as totalPayments, " +
            "COALESCE(AVG(i.contributionPercent), 0) as averageContribution) " +
            "FROM Initiative i WHERE i.parentId = :programId AND i.statusCode = :statusCode")
    java.util.Map<String, Object> getInitiativeSummaryByProgram(@Param("programId") Long programId,
                                                                @Param("statusCode") String statusCode);
}