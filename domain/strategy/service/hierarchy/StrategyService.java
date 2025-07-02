package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.StrategyResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public Page<StrategyResponseDto> getAllActiveStrategies(Pageable pageable) {
        log.debug("Fetching active strategies - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Strategy> strategies = strategyRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return strategies.map(this::convertToResponseDto);
    }

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
        log.debug("Searching strategies with query: '{}'", query);

        Page<Strategy> strategies = strategyRepository.searchStrategies(query, ACTIVE_STATUS, pageable);
        return strategies.map(this::convertToResponseDto);
    }

    public Map<String, Object> getStrategySummary() {
        log.debug("Fetching strategy summary");

        return strategyRepository.getStrategySummary(ACTIVE_STATUS);
    }

    // Helper method to convert Strategy entity to DTO
    private StrategyResponseDto convertToResponseDto(Strategy strategy) {
        LocalDate now = LocalDate.now();
        boolean isActive = strategy.isWithinTimeline(now);

        return StrategyResponseDto.builder()
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
                .isActive(isActive)
                .timelineStatus(isActive ? "ACTIVE" : "INACTIVE")
                .build();
    }
}