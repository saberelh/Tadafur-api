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
import java.util.Map;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    /* ───────────── BASIC LISTING ───────────── */
    List<Strategy> findAllByOrderByIdDesc();
    Page<Strategy> findAllByOrderByIdDesc(Pageable pageable);

    /* ───────────── BY OWNER ───────────── */
    List<Strategy> findByOwnerIdOrderByIdDesc(Long ownerId);
    Page<Strategy> findByOwnerIdOrderByIdDesc(Long ownerId, Pageable pageable);

    /* ───────────── SEARCH ───────────── */
    @Query("""
           SELECT s FROM Strategy s
           WHERE LOWER(s.primaryName)        LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.secondaryName)       LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.primaryDescription)  LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.secondaryDescription)LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.vision)              LIKE LOWER(CONCAT('%', :q, '%'))
           ORDER BY s.id DESC
           """)
    Page<Strategy> search(@Param("q") String query, Pageable pageable);

    @Query("""
           SELECT s FROM Strategy s
           WHERE LOWER(s.primaryName)        LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.secondaryName)       LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.primaryDescription)  LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.secondaryDescription)LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(s.vision)              LIKE LOWER(CONCAT('%', :q, '%'))
           ORDER BY s.id DESC
           """)
    List<Strategy> searchList(@Param("q") String query);

    /* ───────────── ACTIVE (timeline overlaps “today”) ───────────── */
    @Query("""
           SELECT s FROM Strategy s
           WHERE (s.timelineFrom IS NULL OR s.timelineFrom <= CURRENT_DATE)
             AND (s.timelineTo   IS NULL OR s.timelineTo   >= CURRENT_DATE)
           ORDER BY s.timelineFrom ASC
           """)
    List<Strategy> findCurrentlyActive();

    @Query("""
           SELECT s FROM Strategy s
           WHERE (s.timelineFrom IS NULL OR s.timelineFrom <= CURRENT_DATE)
             AND (s.timelineTo   IS NULL OR s.timelineTo   >= CURRENT_DATE)
           ORDER BY s.timelineFrom ASC
           """)
    Page<Strategy> findCurrentlyActive(Pageable pageable);

    /* ───────────── BY TIMELINE RANGE ───────────── */
    @Query("""
           SELECT s FROM Strategy s
           WHERE (s.timelineFrom >= :fromDate AND s.timelineFrom <= :toDate)
              OR (s.timelineTo   >= :fromDate AND s.timelineTo   <= :toDate)
              OR (s.timelineFrom <= :fromDate AND s.timelineTo   >= :toDate)
           ORDER BY s.timelineFrom ASC
           """)
    List<Strategy> findByTimelineRange(@Param("fromDate") LocalDate from,
                                       @Param("toDate")   LocalDate to);

    @Query("""
           SELECT s FROM Strategy s
           WHERE (s.timelineFrom >= :fromDate AND s.timelineFrom <= :toDate)
              OR (s.timelineTo   >= :fromDate AND s.timelineTo   <= :toDate)
              OR (s.timelineFrom <= :fromDate AND s.timelineTo   >= :toDate)
           ORDER BY s.timelineFrom ASC
           """)
    Page<Strategy> findByTimelineRange(@Param("fromDate") LocalDate from,
                                       @Param("toDate")   LocalDate to,
                                       Pageable pageable);

    /* ───────────── SUMMARY QUERIES ───────────── */
    @Query("SELECT COUNT(s) FROM Strategy s WHERE s.ownerId = :ownerId")
    long countByOwnerId(@Param("ownerId") Long ownerId);

    @Query("""
           SELECT new map(
               COUNT(s)                                   AS totalCount,
               COALESCE(SUM(s.plannedTotalBudget), 0)     AS totalPlannedBudget,
               COALESCE(SUM(s.calculatedTotalBudget),0)   AS totalCalculatedBudget,
               COALESCE(SUM(s.calculatedTotalPayments),0) AS totalPayments)
           FROM Strategy s
           """)
    Map<String,Object> getGlobalSummary();

    @Query("""
           SELECT new map(
               COUNT(s)                                   AS totalCount,
               COALESCE(SUM(s.plannedTotalBudget), 0)     AS totalPlannedBudget,
               COALESCE(SUM(s.calculatedTotalPayments),0) AS totalPayments)
           FROM Strategy s
           WHERE s.ownerId = :ownerId
           """)
    Map<String,Object> getSummaryByOwner(@Param("ownerId") Long ownerId);
}
