package com.project.Tadafur_api.domain.strategy.service;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepository;

    // ===== BASIC READ OPERATIONS =====

    public List<Strategy> getAllActiveStrategies() {
        log.info("Fetching all active strategies");
        return strategyRepository.findByStatusCodeOrderByCreatedAtDesc("ACTIVE");
    }

    public Page<Strategy> getAllActiveStrategies(int page, int size) {
        log.info("Fetching active strategies - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return strategyRepository.findByStatusCodeOrderByCreatedAtDesc("ACTIVE", pageable);
    }

    public Optional<Strategy> getStrategyById(Long id) {
        log.info("Fetching strategy by id: {}", id);
        return strategyRepository.findByIdAndStatusCode(id, "ACTIVE");
    }

    // ===== DASHBOARD ANALYTICS =====

    @Cacheable("strategy-summary")
    public Map<String, Object> getExecutiveSummary() {
        log.info("Generating executive summary");

        Map<String, Object> summary = new HashMap<>();

        // Basic statistics
        Map<String, Object> basicStats = strategyRepository.getStrategySummary();
        summary.put("basicStatistics", basicStats);

        // Budget utilization
        Map<String, Object> budgetStats = strategyRepository.getBudgetUtilizationSummary();
        summary.put("budgetUtilization", budgetStats);

        // Timeline distribution
        Map<String, Object> timelineStats = strategyRepository.getTimelineStatusDistribution();
        summary.put("timelineDistribution", timelineStats);

        // Recent activity
        List<Strategy> recentStrategies = strategyRepository.findRecentStrategies(5);
        summary.put("recentStrategies", recentStrategies);

        // Top strategies by budget
        List<Strategy> topStrategies = strategyRepository.findTopStrategiesByBudget(5);
        summary.put("topStrategiesByBudget", topStrategies);

        return summary;
    }

    @Cacheable("financial-dashboard")
    public Map<String, Object> getFinancialDashboard() {
        log.info("Generating financial dashboard");

        Map<String, Object> dashboard = new HashMap<>();

        // Overall financial summary
        Map<String, Object> summary = strategyRepository.getStrategySummary();
        dashboard.put("summary", summary);

        // Budget utilization details
        Map<String, Object> utilization = strategyRepository.getBudgetUtilizationSummary();
        dashboard.put("utilization", utilization);

        // Budget range distribution
        dashboard.put("budgetRanges", getBudgetRangeDistribution());

        // Authority spending
        List<Map<String, Object>> authoritySpending = strategyRepository.getAuthorityParticipation();
        dashboard.put("authoritySpending", authoritySpending);

        return dashboard;
    }

    @Cacheable("timeline-dashboard")
    public Map<String, Object> getTimelineDashboard() {
        log.info("Generating timeline dashboard");

        Map<String, Object> dashboard = new HashMap<>();

        LocalDate now = LocalDate.now();

        // Current active strategies
        List<Strategy> activeToday = strategyRepository.findActiveStrategiesOnDate(now);
        dashboard.put("activeToday", activeToday);

        // Timeline status distribution
        Map<String, Object> statusDistribution = strategyRepository.getTimelineStatusDistribution();
        dashboard.put("statusDistribution", statusDistribution);

        // Upcoming strategies (next 3 months)
        LocalDate threeMonthsLater = now.plusMonths(3);
        List<Strategy> upcomingStrategies = strategyRepository.findStrategiesByTimelineRange(now, threeMonthsLater);
        dashboard.put("upcomingStrategies", upcomingStrategies);

        // Monthly creation trend
        List<Object[]> creationTrend = strategyRepository.getMonthlyCreationTrend();
        dashboard.put("creationTrend", formatMonthlyTrend(creationTrend));

        return dashboard;
    }

    // ===== SEARCH AND FILTERING =====

    public Page<Strategy> searchStrategies(String query, int page, int size) {
        log.info("Searching strategies with query: '{}' - page: {}, size: {}", query, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return strategyRepository.searchStrategies(query, pageable);
    }

    public List<Strategy> getStrategiesByOwner(Long ownerId) {
        log.info("Fetching strategies by owner: {}", ownerId);
        return strategyRepository.findByOwnerId(ownerId);
    }

    public Page<Strategy> getStrategiesByOwner(Long ownerId, int page, int size) {
        log.info("Fetching strategies by owner: {} - page: {}, size: {}", ownerId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return strategyRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, "ACTIVE", pageable);
    }

    // ===== TEMPORAL QUERIES =====

    public List<Strategy> getStrategiesByYear(int year) {
        log.info("Fetching strategies by year: {}", year);
        return strategyRepository.findStrategiesByYear(year);
    }

    public List<Strategy> getStrategiesByTimelineRange(LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching strategies by timeline range: {} to {}", fromDate, toDate);
        return strategyRepository.findStrategiesByTimelineRange(fromDate, toDate);
    }

    public List<Strategy> getActiveStrategiesOnDate(LocalDate date) {
        log.info("Fetching active strategies on date: {}", date);
        return strategyRepository.findActiveStrategiesOnDate(date);
    }

    // ===== FINANCIAL QUERIES =====

    public List<Strategy> getStrategiesByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        log.info("Fetching strategies by budget range: {} to {}", minBudget, maxBudget);
        return strategyRepository.findStrategiesByBudgetRange(minBudget, maxBudget);
    }

    public List<Strategy> getTopStrategiesByBudget(int limit) {
        log.info("Fetching top {} strategies by budget", limit);
        return strategyRepository.findTopStrategiesByBudget(limit);
    }

    // ===== ANALYTICS HELPERS =====

    @Cacheable("authority-participation")
    public List<Map<String, Object>> getAuthorityParticipation() {
        log.info("Fetching authority participation data");
        return strategyRepository.getAuthorityParticipation();
    }

    public Map<String, Object> getStrategyHealth(Long strategyId) {
        log.info("Calculating strategy health for id: {}", strategyId);

        Optional<Strategy> strategyOpt = getStrategyById(strategyId);
        if (strategyOpt.isEmpty()) {
            return Map.of("error", "Strategy not found");
        }

        Strategy strategy = strategyOpt.get();
        Map<String, Object> health = new HashMap<>();

        // Timeline health
        health.put("timelineStatus", strategy.getTimelineStatus());
        health.put("isOnTime", !"OVERDUE".equals(strategy.getTimelineStatus()));

        // Budget health
        BigDecimal utilization = strategy.getBudgetUtilization();
        health.put("budgetUtilization", utilization);
        health.put("budgetHealth", calculateBudgetHealth(utilization));

        // Overall health score (0-100)
        health.put("overallScore", calculateOverallHealthScore(strategy));

        return health;
    }

    // ===== PRIVATE HELPER METHODS =====

    private Map<String, Integer> getBudgetRangeDistribution() {
        List<Strategy> strategies = getAllActiveStrategies();
        Map<String, Integer> distribution = new HashMap<>();

        distribution.put("under1M", 0);
        distribution.put("1M_to_5M", 0);
        distribution.put("5M_to_10M", 0);
        distribution.put("over10M", 0);

        for (Strategy strategy : strategies) {
            BigDecimal budget = strategy.getPlannedTotalBudget();
            if (budget == null) continue;

            if (budget.compareTo(BigDecimal.valueOf(1_000_000)) < 0) {
                distribution.merge("under1M", 1, Integer::sum);
            } else if (budget.compareTo(BigDecimal.valueOf(5_000_000)) < 0) {
                distribution.merge("1M_to_5M", 1, Integer::sum);
            } else if (budget.compareTo(BigDecimal.valueOf(10_000_000)) < 0) {
                distribution.merge("5M_to_10M", 1, Integer::sum);
            } else {
                distribution.merge("over10M", 1, Integer::sum);
            }
        }

        return distribution;
    }

    private List<Map<String, Object>> formatMonthlyTrend(List<Object[]> rawData) {
        List<Map<String, Object>> trend = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", row[0]); // Date
            monthData.put("count", row[1]); // Count
            trend.add(monthData);
        }

        return trend;
    }

    private String calculateBudgetHealth(BigDecimal utilization) {
        if (utilization == null) return "UNKNOWN";

        if (utilization.compareTo(BigDecimal.valueOf(110)) > 0) {
            return "OVER_BUDGET";
        } else if (utilization.compareTo(BigDecimal.valueOf(90)) > 0) {
            return "ON_TARGET";
        } else if (utilization.compareTo(BigDecimal.valueOf(50)) > 0) {
            return "UNDER_UTILIZED";
        } else {
            return "MINIMAL_SPEND";
        }
    }

    private int calculateOverallHealthScore(Strategy strategy) {
        int score = 50; // Base score

        // Timeline factor (30 points)
        String timelineStatus = strategy.getTimelineStatus();
        switch (timelineStatus) {
            case "ACTIVE" -> score += 30;
            case "UPCOMING" -> score += 25;
            case "COMPLETED" -> score += 20;
            default -> score += 10;
        }

        // Budget utilization factor (20 points)
        BigDecimal utilization = strategy.getBudgetUtilization();
        if (utilization != null) {
            if (utilization.compareTo(BigDecimal.valueOf(80)) >= 0 &&
                    utilization.compareTo(BigDecimal.valueOf(100)) <= 0) {
                score += 20; // Optimal range
            } else if (utilization.compareTo(BigDecimal.valueOf(100)) > 0) {
                score += 10; // Over budget
            } else {
                score += 15; // Under utilized but still positive
            }
        }

        return Math.min(100, Math.max(0, score));
    }
}