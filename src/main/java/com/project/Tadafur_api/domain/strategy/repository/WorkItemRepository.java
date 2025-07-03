package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.WorkItem;
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
public interface WorkItemRepository extends JpaRepository<WorkItem, Long> {

    // Basic queries
    List<WorkItem> findByStatusCodeOrderByCreatedAtDesc(String statusCode);

    Page<WorkItem> findByStatusCodeOrderByCreatedAtDesc(String statusCode, Pageable pageable);

    Optional<WorkItem> findByIdAndStatusCode(Long id, String statusCode);

    // Project-based queries
    List<WorkItem> findByProjectIdAndStatusCodeOrderByItemSortAsc(Long projectId, String statusCode);

    Page<WorkItem> findByProjectIdAndStatusCodeOrderByItemSortAsc(Long projectId, String statusCode, Pageable pageable);

    // Parent-based queries (for subtasks)
    List<WorkItem> findByParentIdAndStatusCodeOrderByItemSortAsc(Long parentId, String statusCode);

    // Assignee-based queries
    List<WorkItem> findByAssigneeUserIdAndStatusCodeOrderByPlannedDueDateAsc(Long assigneeUserId, String statusCode);

    Page<WorkItem> findByAssigneeUserIdAndStatusCodeOrderByPlannedDueDateAsc(Long assigneeUserId, String statusCode, Pageable pageable);

    // Search queries - Updated for new field names
    @Query("SELECT w FROM WorkItem w WHERE " +
            "(LOWER(w.primaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(w.secondaryName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(w.primaryDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(w.secondaryDescription) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.createdAt DESC")
    Page<WorkItem> searchWorkItems(@Param("query") String query,
                                   @Param("statusCode") String statusCode,
                                   Pageable pageable);

    // Priority and status queries
    List<WorkItem> findByPriorityIdAndStatusCodeOrderByPlannedDueDateAsc(Long priorityId, String statusCode);

    List<WorkItem> findByStatusIdAndStatusCodeOrderByPlannedDueDateAsc(Long statusId, String statusCode);

    // Due date queries
    @Query("SELECT w FROM WorkItem w WHERE " +
            "w.plannedDueDate = :date AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.priorityId DESC")
    List<WorkItem> findWorkItemsDueOnDate(@Param("date") LocalDate date,
                                          @Param("statusCode") String statusCode);

    @Query("SELECT w FROM WorkItem w WHERE " +
            "w.plannedDueDate BETWEEN :fromDate AND :toDate AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.plannedDueDate ASC")
    List<WorkItem> findWorkItemsDueInRange(@Param("fromDate") LocalDate fromDate,
                                           @Param("toDate") LocalDate toDate,
                                           @Param("statusCode") String statusCode);

    // Overdue work items
    @Query("SELECT w FROM WorkItem w WHERE " +
            "w.plannedDueDate < :currentDate AND " +
            "w.progressPercent < 100 AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.plannedDueDate ASC")
    List<WorkItem> findOverdueWorkItems(@Param("currentDate") LocalDate currentDate,
                                        @Param("statusCode") String statusCode);

    // Progress-based queries
    @Query("SELECT w FROM WorkItem w WHERE " +
            "w.progressPercent >= :minProgress AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.progressPercent DESC")
    List<WorkItem> findByMinimumProgress(@Param("minProgress") java.math.BigDecimal minProgress,
                                         @Param("statusCode") String statusCode);

    // Completed work items
    @Query("SELECT w FROM WorkItem w WHERE " +
            "w.progressPercent >= 100 AND " +
            "w.statusCode = :statusCode " +
            "ORDER BY w.actualDueDate DESC")
    List<WorkItem> findCompletedWorkItems(@Param("statusCode") String statusCode);

    // Work item group queries
    List<WorkItem> findByWorkItemGroupIdAndStatusCodeOrderByItemSortAsc(Long groupId, String statusCode);

    // Level-based queries (for hierarchy)
    List<WorkItem> findByProjectIdAndLevelAndStatusCodeOrderByItemSortAsc(Long projectId, Integer level, String statusCode);

    // Count queries
    @Query("SELECT COUNT(w) FROM WorkItem w WHERE w.projectId = :projectId AND w.statusCode = :statusCode")
    long countByProjectId(@Param("projectId") Long projectId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(w) FROM WorkItem w WHERE w.assigneeUserId = :assigneeUserId AND w.statusCode = :statusCode")
    long countByAssigneeUserId(@Param("assigneeUserId") Long assigneeUserId, @Param("statusCode") String statusCode);

    @Query("SELECT COUNT(w) FROM WorkItem w WHERE w.parentId = :parentId AND w.statusCode = :statusCode")
    long countByParentId(@Param("parentId") Long parentId, @Param("statusCode") String statusCode);

    // Summary queries
    @Query("SELECT new map(COUNT(w) as totalCount, " +
            "COALESCE(AVG(w.progressPercent), 0) as averageProgress, " +
            "COALESCE(SUM(w.estimatedTime), 0) as totalEstimatedTime, " +
            "COALESCE(SUM(w.actualTime), 0) as totalActualTime, " +
            "COUNT(CASE WHEN w.progressPercent >= 100 THEN 1 END) as completedCount) " +
            "FROM WorkItem w WHERE w.projectId = :projectId AND w.statusCode = :statusCode")
    java.util.Map<String, Object> getWorkItemSummaryByProject(@Param("projectId") Long projectId,
                                                              @Param("statusCode") String statusCode);

    // Assignee summary
    @Query("SELECT new map(COUNT(w) as totalCount, " +
            "COALESCE(AVG(w.progressPercent), 0) as averageProgress, " +
            "COUNT(CASE WHEN w.plannedDueDate < CURRENT_DATE AND w.progressPercent < 100 THEN 1 END) as overdueCount) " +
            "FROM WorkItem w WHERE w.assigneeUserId = :assigneeUserId AND w.statusCode = :statusCode")
    java.util.Map<String, Object> getWorkItemSummaryByAssignee(@Param("assigneeUserId") Long assigneeUserId,
                                                               @Param("statusCode") String statusCode);
}