package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.StrategyResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "strategies", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<StrategyResponseDto> getAllActiveStrategies(Pageable pageable) {
        log.debug("Fetching active strategies - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Strategy> strategies = strategyRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return strategies.map(this::convertToResponseDto);
    }

    @Cacheable(value = "strategy-details", key = "#id")
    public StrategyResponseDto getStrategyById(Long id) {
        log.debug("Fetching strategy by ID: {}", id);

        Strategy strategy = strategyRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", id));

        return convertToResponseDto(strategy);
    }

    public List<StrategyResponseDto> getStrategiesByOwner(Long ownerId) {
        log.debug("Fetching strategies for owner: {}", ownerId);

        List<Strategy> strategies = strategyRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<StrategyResponseDto> searchStrategies(String query, Pageable pageable) {
        log.debug("Searching strategies with query: '{}', page: {}, size: {}", query, pageable.getPageNumber(), pageable.getPageSize());

        Page<Strategy> strategies = strategyRepository.searchStrategies(query, ACTIVE_STATUS, pageable);
        return strategies.map(this::convertToResponseDto);
    }

    public List<StrategyResponseDto> getActiveStrategiesOnDate(LocalDate date) {
        log.debug("Fetching strategies active on date: {}", date);

        List<Strategy> strategies = strategyRepository.findActiveStrategiesOnDate(date, ACTIVE_STATUS);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<StrategyResponseDto> getStrategiesByTimelineRange(LocalDate fromDate, LocalDate toDate) {
        log.debug("Fetching strategies within timeline range: {} to {}", fromDate, toDate);

        List<Strategy> strategies = strategyRepository.findStrategiesByTimelineRange(fromDate, toDate, ACTIVE_STATUS);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<StrategyResponseDto> getStrategiesByYear(int year) {
        log.debug("Fetching strategies for year: {}", year);

        List<Strategy> strategies = strategyRepository.findStrategiesByYear(year, ACTIVE_STATUS);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<StrategyResponseDto> getRecentStrategies(int limit) {
        log.debug("Fetching recent strategies, limit: {}", limit);

        List<Strategy> strategies = strategyRepository.findRecentStrategies(ACTIVE_STATUS, limit);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "strategy-summary")
    public Map<String, Object> getStrategySummary() {
        log.debug("Fetching strategy summary");

        return strategyRepository.getStrategySummary(ACTIVE_STATUS);
    }

    public Page<StrategyResponseDto> findByMultiLanguageNames(String primaryName, String secondaryName, Pageable pageable) {
        log.debug("Searching strategies by names - primary: '{}', secondary: '{}'", primaryName, secondaryName);

        Page<Strategy> strategies = strategyRepository.findByMultiLanguageNames(primaryName, secondaryName, ACTIVE_STATUS, pageable);
        return strategies.map(this::convertToResponseDto);
    }

    public List<StrategyResponseDto> getStrategiesByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        log.debug("Fetching strategies by budget range: {} to {}", minBudget, maxBudget);

        List<Strategy> strategies = strategyRepository.findStrategiesByBudgetRange(minBudget, maxBudget, ACTIVE_STATUS);
        return strategies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public long countStrategiesByOwner(Long ownerId) {
        log.debug("Counting strategies for owner: {}", ownerId);

        return strategyRepository.countByOwnerIdAndStatusCode(ownerId, ACTIVE_STATUS);
    }

    public long countActiveStrategies() {
        log.debug("Counting all active strategies");

        return strategyRepository.countByStatusCode(ACTIVE_STATUS);
    }

    // Helper method to convert Strategy entity to DTO
    private StrategyResponseDto convertToResponseDto(Strategy strategy) {
        StrategyResponseDto dto = StrategyResponseDto.builder()
                .id(strategy.getId())
                .primaryName(strategy.getPrimaryName())
                .secondaryName(strategy.getSecondaryName())
                .primaryDescription(strategy.getPrimaryDescription())
                .secondaryDescription(strategy.getSecondaryDescription())
                .vision(strategy.getVision())
                .ownerId(strategy.getOwnerId())
                .timelineFrom(strategy.getTimelineFrom())
                .timelineTo(strategy.getTimelineTo())
                .plannedTotalBudget(strategy.getPlannedTotalBudget())
                .calculatedTotalBudget(strategy.getCalculatedTotalBudget())
                .calculatedTotalPayments(strategy.getCalculatedTotalPayments())
                .budgetUtilization(strategy.getBudgetUtilization())
                .createdBy(strategy.getCreatedBy())
                .createdAt(strategy.getCreatedAt())
                .lastModifiedBy(strategy.getLastModifiedBy())
                .lastModifiedAt(strategy.getLastModifiedAt())
                .statusCode(strategy.getStatusCode())
                .build();

        // Calculate timeline status
        enrichWithTimelineStatus(dto, strategy);

        // Calculate aggregated data
        enrichWithAggregatedData(dto, strategy);

        return dto;
    }

    private void enrichWithTimelineStatus(StrategyResponseDto dto, Strategy strategy) {
        LocalDate now = LocalDate.now();

        if (strategy.getTimelineFrom() != null && strategy.getTimelineTo() != null) {
            boolean isActive = strategy.isWithinTimeline(now);
            dto.setIsActive(isActive);

            if (isActive) {
                long daysRemaining = ChronoUnit.DAYS.between(now, strategy.getTimelineTo());
                dto.setDaysRemaining(daysRemaining);
                dto.setTimelineStatus(daysRemaining > 30 ? "ON_TRACK" : "APPROACHING_DEADLINE");
            } else if (now.isBefore(strategy.getTimelineFrom())) {
                dto.setTimelineStatus("FUTURE");
                dto.setDaysRemaining(ChronoUnit.DAYS.between(now, strategy.getTimelineFrom()));
            } else {
                dto.setTimelineStatus("COMPLETED");
                dto.setDaysRemaining(0L);
            }
        } else {
            dto.setIsActive(false);
            dto.setTimelineStatus("NO_TIMELINE");
            dto.setDaysRemaining(null);
        }
    }

    private void enrichWithAggregatedData(StrategyResponseDto dto, Strategy strategy) {
        // These would require additional repository calls
        // For now, setting default values - implement as needed
        dto.setPerspectiveCount(0);
        dto.setTotalGoalCount(0);
        dto.setTotalProjectCount(0);
        dto.setOverallProgress(BigDecimal.ZERO);

        // TODO: Implement these with additional queries:
        // dto.setPerspectiveCount(perspectiveRepository.countByStrategyId(strategy.getId()));
        // dto.setTotalGoalCount(goalRepository.countByStrategyIdRecursive(strategy.getId()));
        // dto.setTotalProjectCount(projectRepository.countByStrategyIdRecursive(strategy.getId()));
        // dto.setOverallProgress(calculateOverallProgress(strategy.getId()));
    }

    // Additional helper methods for future implementation
    private BigDecimal calculateOverallProgress(Long strategyId) {
        // Implement progress calculation logic
        // This would involve aggregating progress from all levels of the hierarchy
        return BigDecimal.ZERO;
    }

    // Validation methods
    public boolean validateStrategyTimeline(LocalDate timelineFrom, LocalDate timelineTo) {
        if (timelineFrom == null || timelineTo == null) {
            return false;
        }
        return !timelineFrom.isAfter(timelineTo);
    }

    public boolean validateBudgetAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}