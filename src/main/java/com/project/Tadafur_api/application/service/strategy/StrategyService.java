// File: application/service/strategy/StrategyService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.common.PaginationDto;
import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.mapper.common.PaginationMapper;
import com.project.Tadafur_api.application.mapper.strategy.StrategyMapper;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import com.project.Tadafur_api.shared.constants.DomainConstants;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Strategy Service - Business logic for Strategy operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepository;
    private final StrategyMapper strategyMapper;
    private final PaginationMapper paginationMapper;

    /**
     * Get all strategies with pagination
     * GET /api/v1/strategies
     */
    public Map<String, Object> getAll(int page, int size, String sortBy, String sortDirection) {
        log.debug("Getting all strategies - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Strategy> strategyPage = strategyRepository.findAllByOrderByIdDesc(pageable);

        List<StrategyResponseDto> strategies = strategyMapper.toResponseDtoList(strategyPage.getContent());
        PaginationDto pagination = paginationMapper.toPaginationDto(strategyPage);

        Map<String, Object> response = new HashMap<>();
        response.put("strategies", strategies);
        response.put("pagination", pagination);

        log.info("Retrieved {} strategies", strategies.size());
        return response;
    }

    /**
     * Get strategy by ID
     * GET /api/v1/strategies/{id}
     */
    public StrategyResponseDto getById(Long id) {
        log.debug("Getting strategy by id: {}", id);

        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", id));

        log.info("Retrieved strategy with id: {}", id);
        return strategyMapper.toResponseDto(strategy);
    }

    /**
     * Search strategies
     * GET /api/v1/strategies/search?query={query}
     */
    public Map<String, Object> search(String query, int page, int size) {
        log.debug("Searching strategies with query: {}", query);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Strategy> strategyPage = strategyRepository.searchStrategies(query, pageable);

        List<StrategyResponseDto> strategies = strategyMapper.toResponseDtoList(strategyPage.getContent());
        PaginationDto pagination = paginationMapper.toPaginationDto(strategyPage);

        Map<String, Object> response = new HashMap<>();
        response.put("strategies", strategies);
        response.put("pagination", pagination);
        response.put("query", query);

        log.info("Found {} strategies matching query: {}", strategies.size(), query);
        return response;
    }

    /**
     * Get strategies by owner
     * GET /api/v1/strategies/by-owner/{ownerId}
     */
    public Map<String, Object> getByOwner(Long ownerId, int page, int size) {
        log.debug("Getting strategies for owner: {}", ownerId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Strategy> strategyPage = strategyRepository.findByOwnerIdOrderByIdDesc(ownerId, pageable);

        List<StrategyResponseDto> strategies = strategyMapper.toResponseDtoList(strategyPage.getContent());
        PaginationDto pagination = paginationMapper.toPaginationDto(strategyPage);

        // Get owner summary
        Map<String, Object> ownerSummary = strategyRepository.getStrategySummaryByOwner(ownerId);

        Map<String, Object> response = new HashMap<>();
        response.put("strategies", strategies);
        response.put("pagination", pagination);
        response.put("ownerId", ownerId);
        response.put("summary", ownerSummary);

        log.info("Retrieved {} strategies for owner: {}", strategies.size(), ownerId);
        return response;
    }

    /**
     * Get active strategies
     * GET /api/v1/strategies/active
     */
    public Map<String, Object> getActive(int page, int size) {
        log.debug("Getting active strategies");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timelineFrom"));
        Page<Strategy> strategyPage = strategyRepository.findActiveStrategies(pageable);

        List<StrategyResponseDto> strategies = strategyMapper.toResponseDtoList(strategyPage.getContent());
        PaginationDto pagination = paginationMapper.toPaginationDto(strategyPage);

        Map<String, Object> response = new HashMap<>();
        response.put("strategies", strategies);
        response.put("pagination", pagination);
        response.put("currentDate", LocalDate.now());

        log.info("Retrieved {} active strategies", strategies.size());
        return response;
    }

    /**
     * Get strategies by timeline
     * GET /api/v1/strategies/by-timeline?from={date}&to={date}
     */
    public Map<String, Object> getByTimeline(LocalDate from, LocalDate to, int page, int size) {
        log.debug("Getting strategies by timeline - from: {}, to: {}", from, to);

        // Validate dates
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before or equal to To date");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timelineFrom"));
        Page<Strategy> strategyPage = strategyRepository.findStrategiesByTimelineRange(
                from, to, pageable);

        List<StrategyResponseDto> strategies = strategyMapper.toResponseDtoList(strategyPage.getContent());
        PaginationDto pagination = paginationMapper.toPaginationDto(strategyPage);

        Map<String, Object> response = new HashMap<>();
        response.put("strategies", strategies);
        response.put("pagination", pagination);
        response.put("timelineFrom", from);
        response.put("timelineTo", to);

        log.info("Retrieved {} strategies within timeline range", strategies.size());
        return response;
    }

    /**
     * Get strategy summary statistics
     */
    public Map<String, Object> getSummary() {
        log.debug("Getting strategy summary");

        Map<String, Object> summary = strategyRepository.getStrategySummary();

        log.info("Retrieved strategy summary");
        return summary;
    }
}