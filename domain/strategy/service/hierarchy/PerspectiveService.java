package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.PerspectiveResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import com.project.Tadafur_api.domain.strategy.repository.PerspectiveRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PerspectiveService {

    private final PerspectiveRepository perspectiveRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "perspectives", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<PerspectiveResponseDto> getAllActivePerspectives(Pageable pageable) {
        log.debug("Fetching active perspectives - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Perspective> perspectives = perspectiveRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return perspectives.map(this::convertToResponseDto);
    }

    @Cacheable(value = "perspective-details", key = "#id")
    public PerspectiveResponseDto getPerspectiveById(Long id) {
        log.debug("Fetching perspective by ID: {}", id);

        Perspective perspective = perspectiveRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Perspective", "id", id));

        return convertToResponseDto(perspective);
    }

    public List<PerspectiveResponseDto> getPerspectivesByStrategy(Long strategyId) {
        log.debug("Fetching perspectives for strategy: {}", strategyId);

        List<Perspective> perspectives = perspectiveRepository.findByParentIdAndStatusCodeOrderByCreatedAtDesc(strategyId, ACTIVE_STATUS);
        return perspectives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<PerspectiveResponseDto> getPerspectivesByOwner(Long ownerId) {
        log.debug("Fetching perspectives for owner: {}", ownerId);

        List<Perspective> perspectives = perspectiveRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return perspectives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<PerspectiveResponseDto> searchPerspectives(String query, Pageable pageable) {
        log.debug("Searching perspectives with query: '{}'", query);

        Page<Perspective> perspectives = perspectiveRepository.searchPerspectives(query, ACTIVE_STATUS, pageable);
        return perspectives.map(this::convertToResponseDto);
    }

    public List<PerspectiveResponseDto> getPerspectivesByPlanningStatus(String planningStatus) {
        log.debug("Fetching perspectives by planning status: {}", planningStatus);

        List<Perspective> perspectives = perspectiveRepository.findByPlanningStatus(planningStatus, ACTIVE_STATUS);
        return perspectives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<PerspectiveResponseDto> getPerspectivesByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        log.debug("Fetching perspectives by budget range: {} to {}", minBudget, maxBudget);

        List<Perspective> perspectives = perspectiveRepository.findPerspectivesByBudgetRange(minBudget, maxBudget, ACTIVE_STATUS);
        return perspectives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPerspectiveSummaryByStrategy(Long strategyId) {
        log.debug("Fetching perspective summary for strategy: {}", strategyId);

        return perspectiveRepository.getPerspectiveSummaryByStrategy(strategyId, ACTIVE_STATUS);
    }

    public long countPerspectivesByStrategy(Long strategyId) {
        log.debug("Counting perspectives for strategy: {}", strategyId);

        return perspectiveRepository.countByStrategyId(strategyId, ACTIVE_STATUS);
    }

    public long countPerspectivesByOwner(Long ownerId) {
        log.debug("Counting perspectives for owner: {}", ownerId);

        return perspectiveRepository.countByOwnerIdAndStatusCode(ownerId, ACTIVE_STATUS);
    }

    // Helper method to convert Perspective entity to DTO
    private PerspectiveResponseDto convertToResponseDto(Perspective perspective) {
        PerspectiveResponseDto dto = PerspectiveResponseDto.builder()
                .id(perspective.getId())
                .primaryName(perspective.getPrimaryName())
                .secondaryName(perspective.getSecondaryName())
                .primaryDescription(perspective.getPrimaryDescription())
                .secondaryDescription(perspective.getSecondaryDescription())
                .ownerId(perspective.getOwnerId())
                .parentId(perspective.getParentId())
                .planningStatusCode(perspective.getPlanningStatusCode())
                .progressStatusCode(perspective.getProgressStatusCode())
                .calculatedTotalBudget(perspective.getCalculatedTotalBudget())
                .calculatedTotalPayments(perspective.getCalculatedTotalPayments())
                .plannedTotalBudget(perspective.getPlannedTotalBudget())
                .budgetUtilization(perspective.getBudgetUtilization())
                .createdBy(perspective.getCreatedBy())
                .createdAt(perspective.getCreatedAt())
                .lastModifiedBy(perspective.getLastModifiedBy())
                .lastModifiedAt(perspective.getLastModifiedAt())
                .statusCode(perspective.getStatusCode())
                .build();

        // Calculate aggregated data
        enrichWithAggregatedData(dto, perspective);

        return dto;
    }

    private void enrichWithAggregatedData(PerspectiveResponseDto dto, Perspective perspective) {
        // These would require additional repository calls
        dto.setGoalCount(0);
        dto.setTotalInitiativeCount(0);
        dto.setTotalProjectCount(0);
        dto.setAverageProgress(BigDecimal.ZERO);

        // TODO: Implement with additional queries
    }

    // Validation methods
    public boolean validatePerspectiveBudget(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}