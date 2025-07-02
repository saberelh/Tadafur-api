package com.project.Tadafur_api.domain.strategy.controller.hierarchy;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategy")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Strategy APIs", description = "READ-ONLY Strategic Planning APIs for Dashboard & Analytics")
public class StrategyController {

    private final StrategyService strategyService;

    // ===== BASIC STRATEGY OPERATIONS =====

    @GetMapping("/strategies")
    @Operation(summary = "Get All Strategies", description = "Retrieve all active strategies with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Strategies retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<Strategy>> getAllStrategies(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/strategy/strategies - page: {}, size: {}", page, size);
        Page<Strategy> strategies = strategyService.getAllActiveStrategies(page, size);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/strategies/{id}")
    @Operation(summary = "Get Strategy by ID", description = "Retrieve a specific strategy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Strategy found"),
            @ApiResponse(responseCode = "404", description = "Strategy not found")
    })
    public ResponseEntity<Strategy> getStrategyById(
            @Parameter(description = "Strategy ID")
            @PathVariable Long id) {

        log.info("GET /api/strategy/strategies/{}", id);
        return strategyService.getStrategyById(id)
                .map(strategy -> ResponseEntity.ok(strategy))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== DASHBOARD ANALYTICS ENDPOINTS =====

    @GetMapping("/dashboard/executive")
    @Operation(summary = "Executive Dashboard", description = "Comprehensive executive summary and key metrics")
    @ApiResponse(responseCode = "200", description = "Executive dashboard data retrieved successfully")
    public ResponseEntity<Map<String, Object>> getExecutiveDashboard() {
        log.info("GET /api/strategy/dashboard/executive");
        Map<String, Object> dashboard = strategyService.getExecutiveSummary();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/dashboard/financial")
    @Operation(summary = "Financial Dashboard", description = "Financial analytics and budget insights")
    @ApiResponse(responseCode = "200", description = "Financial dashboard data retrieved successfully")
    public ResponseEntity<Map<String, Object>> getFinancialDashboard() {
        log.info("GET /api/strategy/dashboard/financial");
        Map<String, Object> dashboard = strategyService.getFinancialDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/dashboard/timeline")
    @Operation(summary = "Timeline Dashboard", description = "Timeline analytics and scheduling insights")
    @ApiResponse(responseCode = "200", description = "Timeline dashboard data retrieved successfully")
    public ResponseEntity<Map<String, Object>> getTimelineDashboard() {
        log.info("GET /api/strategy/dashboard/timeline");
        Map<String, Object> dashboard = strategyService.getTimelineDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/strategies/{id}/health")
    @Operation(summary = "Strategy Health Check", description = "Comprehensive health assessment for a strategy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Health data retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Strategy not found")
    })
    public ResponseEntity<Map<String, Object>> getStrategyHealth(
            @Parameter(description = "Strategy ID")
            @PathVariable Long id) {

        log.info("GET /api/strategy/strategies/{}/health", id);
        Map<String, Object> health = strategyService.getStrategyHealth(id);

        if (health.containsKey("error")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(health);
    }

    // ===== SEARCH AND FILTERING =====

    @GetMapping("/strategies/search")
    @Operation(summary = "Search Strategies", description = "Search strategies by name or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<Page<Strategy>> searchStrategies(
            @Parameter(description = "Search query")
            @RequestParam String query,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/strategy/strategies/search?query={} - page: {}, size: {}", query, page, size);
        Page<Strategy> strategies = strategyService.searchStrategies(query, page, size);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/strategies/by-owner/{ownerId}")
    @Operation(summary = "Get Strategies by Owner", description = "Retrieve strategies owned by specific authority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Strategies retrieved successfully")
    })
    public ResponseEntity<Page<Strategy>> getStrategiesByOwner(
            @Parameter(description = "Owner Authority ID")
            @PathVariable Long ownerId,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/strategy/strategies/by-owner/{} - page: {}, size: {}", ownerId, page, size);
        Page<Strategy> strategies = strategyService.getStrategiesByOwner(ownerId, page, size);
        return ResponseEntity.ok(strategies);
    }

    // ===== TEMPORAL ANALYTICS =====

    @GetMapping("/strategies/by-year/{year}")
    @Operation(summary = "Get Strategies by Year", description = "Retrieve strategies for specific year")
    @ApiResponse(responseCode = "200", description = "Strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getStrategiesByYear(
            @Parameter(description = "Year (YYYY)")
            @PathVariable int year) {

        log.info("GET /api/strategy/strategies/by-year/{}", year);
        List<Strategy> strategies = strategyService.getStrategiesByYear(year);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/strategies/by-timeline")
    @Operation(summary = "Get Strategies by Timeline", description = "Retrieve strategies within date range")
    @ApiResponse(responseCode = "200", description = "Strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getStrategiesByTimelineRange(
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        log.info("GET /api/strategy/strategies/by-timeline?fromDate={}&toDate={}", fromDate, toDate);
        List<Strategy> strategies = strategyService.getStrategiesByTimelineRange(fromDate, toDate);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/strategies/active-on-date")
    @Operation(summary = "Get Active Strategies on Date", description = "Retrieve strategies active on specific date")
    @ApiResponse(responseCode = "200", description = "Active strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getActiveStrategiesOnDate(
            @Parameter(description = "Date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("GET /api/strategy/strategies/active-on-date?date={}", date);
        List<Strategy> strategies = strategyService.getActiveStrategiesOnDate(date);
        return ResponseEntity.ok(strategies);
    }

    // ===== FINANCIAL ANALYTICS =====

    @GetMapping("/strategies/by-budget-range")
    @Operation(summary = "Get Strategies by Budget Range", description = "Retrieve strategies within budget range")
    @ApiResponse(responseCode = "200", description = "Strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getStrategiesByBudgetRange(
            @Parameter(description = "Minimum budget amount")
            @RequestParam BigDecimal minBudget,
            @Parameter(description = "Maximum budget amount")
            @RequestParam BigDecimal maxBudget) {

        log.info("GET /api/strategy/strategies/by-budget-range?minBudget={}&maxBudget={}", minBudget, maxBudget);
        List<Strategy> strategies = strategyService.getStrategiesByBudgetRange(minBudget, maxBudget);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/strategies/top-by-budget")
    @Operation(summary = "Get Top Strategies by Budget", description = "Retrieve top strategies sorted by budget")
    @ApiResponse(responseCode = "200", description = "Top strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getTopStrategiesByBudget(
            @Parameter(description = "Number of strategies to retrieve")
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /api/strategy/strategies/top-by-budget?limit={}", limit);
        List<Strategy> strategies = strategyService.getTopStrategiesByBudget(limit);
        return ResponseEntity.ok(strategies);
    }

    // ===== COLLABORATION ANALYTICS =====

    @GetMapping("/analytics/authority-participation")
    @Operation(summary = "Authority Participation Analytics", description = "Get participation metrics by authority")
    @ApiResponse(responseCode = "200", description = "Authority participation data retrieved successfully")
    public ResponseEntity<List<Map<String, Object>>> getAuthorityParticipation() {
        log.info("GET /api/strategy/analytics/authority-participation");
        List<Map<String, Object>> participation = strategyService.getAuthorityParticipation();
        return ResponseEntity.ok(participation);
    }

    // ===== QUICK INSIGHTS =====

    @GetMapping("/insights/current-year")
    @Operation(summary = "Current Year Insights", description = "Quick insights for current year strategies")
    @ApiResponse(responseCode = "200", description = "Current year insights retrieved successfully")
    public ResponseEntity<List<Strategy>> getCurrentYearStrategies() {
        int currentYear = LocalDate.now().getYear();
        log.info("GET /api/strategy/insights/current-year ({})", currentYear);
        List<Strategy> strategies = strategyService.getStrategiesByYear(currentYear);
        return ResponseEntity.ok(strategies);
    }

    @GetMapping("/insights/active-today")
    @Operation(summary = "Active Today", description = "Strategies active as of today")
    @ApiResponse(responseCode = "200", description = "Today's active strategies retrieved successfully")
    public ResponseEntity<List<Strategy>> getActiveTodayStrategies() {
        LocalDate today = LocalDate.now();
        log.info("GET /api/strategy/insights/active-today ({})", today);
        List<Strategy> strategies = strategyService.getActiveStrategiesOnDate(today);
        return ResponseEntity.ok(strategies);
    }
}