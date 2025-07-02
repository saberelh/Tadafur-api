package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Project;
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
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Basic queries
    List<Project> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<Project> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<Project> findByIdAndStatusCode(Long id, String statusCode);

    // Parent-based queries (Initiative)
    List<Project> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode);

    Page<Project> findByParentIdAndStatusCodeOrderByCreatedAtDesc(Long parentId, String statusCode, Pageable pageable);

    // Owner-based queries
    List<Project> findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(Long ownerId, String statusCode);

    // Search queries - Updated for new field names
    @Query("SELECT p FROM Project p WHERE " +
            "(LOWER(p.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.createdAt DESC")
    Page<Project> searchProjects(@Param("query") String query,
                                 @Param("statusCode") String statusCode,
                                 Pageable pageable);

    // Timeline-based queries
    @Query("SELECT p FROM Project p WHERE " +
            "p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date) AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.startDate ASC")
    List<Project> findActiveProjectsOnDate(@Param("date") LocalDate date,
                                           @Param("statusCode") String statusCode);

    @Query("SELECT p FROM Project p WHERE " +
            "p.startDate BETWEEN :fromDate AND :toDate AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.startDate ASC")
    List<Project> findProjectsByTimelineRange(@Param("fromDate") LocalDate fromDate,
                                              @Param("toDate") LocalDate toDate,
                                              @Param("statusCode") String statusCode);

    // Progress-based queries
    @Query("SELECT p FROM Project p WHERE " +
            "p.calculatedProgressPercent >= :minProgress AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.calculatedProgressPercent DESC")
    List<Project> findByMinimumProgress(@Param("minProgress") java.math.BigDecimal minProgress,
                                        @Param("statusCode") String statusCode);

    // Priority and status queries
    List<Project> findByPriorityIdAndStatusCodeOrderByCreatedAtDesc(Long priorityId, String statusCode);

    List<Project> findByStatusIdAndStatusCodeOrderByCreatedAtDesc(Long statusId, String statusCode);

    // Methodology-based queries
    List<Project> findByProjectMethodologyIdAndStatusCodeOrderByCreatedAtDesc(Long methodologyId, String statusCode);

    // Vision alignment queries
    @Query("SELECT p FROM Project p WHERE " +
            "p.visionPriorities LIKE CONCAT('%', :visionPriorityId, '%') AND " +
            "p.statusCode = :statusCode")
    List<Project> findByVisionPriority(@Param("visionPriorityId") String visionPriorityId,
                                       @Param("statusCode") String statusCode);

    // Budget-based queries
    @Query("SELECT p FROM Project p WHERE " +
            "p.plannedTotalBudget BETWEEN :minBudget AND :maxBudget AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.plannedTotalBudget DESC")
    List<Project> findProjectsByBudgetRange(@Param("minBudget") java.math.BigDecimal minBudget,
                                            @Param("maxBudget") java.math.BigDecimal maxBudget,
                                            @Param("statusCode") String statusCode);

    // Overdue projects
    @Query("SELECT p FROM Project p WHERE " +
            "p.endDate < :currentDate AND " +
            "p.calculatedProgressPercent < 100 AND " +
            "p.statusCode = :statusCode " +
            "ORDER BY p.endDate ASC")
    List<Project> findOverdueProjects(@Param("currentDate") LocalDate currentDate,
                                      @Param("statusCode") String statusCode);

    // Count queries
    @Query("SELECT COUNT(p) FROM Project p WHERE p.parentId = :initiativeId AND p.statusCode = :statusCode")
    long countByInitiativeId(@Param("initiativeId") Long initiativeId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.ownerId = :ownerId AND p.statusCode = :statusCode")
    long countByOwnerIdAndStatusCode(@Param("ownerId") Long ownerId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.priorityId = :priorityId AND p.statusCode = :statusCode")
    long countByPriorityId(@Param("priorityId") Long priorityId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(p) as totalCount, " +
            "COALESCE(AVG(p.calculatedProgressPercent), 0) as averageProgress, " +
            "COALESCE(SUM(p.plannedTotalBudget), 0) as totalPlannedBudget, " +
            "COALESCE(SUM(p.calculatedTotalPayments), 0) as totalPayments, " +
            "COALESCE(SUM(p.actualCost), 0) as totalActualCost) " +
            "FROM Project p WHERE p.parentId = :initiativeId AND p.statusCode = :statusCode")
    java.util.Map<String, Object> getProjectSummaryByInitiative(@Param("initiativeId") Long initiativeId,
                                                                @Param("statusCode") String statusCode);

    // Type-based queries
    List<Project> findByTypeAndStatusCodeOrderByCreatedAtDesc(Integer type, String statusCode);
}