package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.InitiativeResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Initiative;
import com.project.Tadafur_api.domain.strategy.repository.InitiativeRepository;
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
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "initiatives", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<InitiativeResponseDto> getAllActiveInitiatives(Pageable pageable) {
        log.debug("Fetching active initiatives - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Initiative> initiatives = initiativeRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return initiatives.map(this::convertToResponseDto);
    }

    @Cacheable(value = "initiative-details", key = "#id")
    public InitiativeResponseDto getInitiativeById(Long id) {
        log.debug("Fetching initiative by ID: {}", id);

        Initiative initiative = initiativeRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative", "id", id));

        return convertToResponseDto(initiative);
    }

    public List<InitiativeResponseDto> getInitiativesByProgram(Long programId) {
        log.debug("Fetching initiatives for program: {}", programId);

        List<Initiative> initiatives = initiativeRepository.findByParentIdAndStatusCodeOrderByCreatedAtDesc(programId, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByOwner(Long ownerId) {
        log.debug("Fetching initiatives for owner: {}", ownerId);

        List<Initiative> initiatives = initiativeRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<InitiativeResponseDto> searchInitiatives(String query, Pageable pageable) {
        log.debug("Searching initiatives with query: '{}'", query);

        Page<Initiative> initiatives = initiativeRepository.searchInitiatives(query, ACTIVE_STATUS, pageable);
        return initiatives.map(this::convertToResponseDto);
    }

    public List<InitiativeResponseDto> getInitiativesByMinimumProgress(BigDecimal minProgress) {
        log.debug("Fetching initiatives with minimum progress: {}", minProgress);

        List<Initiative> initiatives = initiativeRepository.findByMinimumProgress(minProgress, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByVisionPriority(String visionPriorityId) {
        log.debug("Fetching initiatives for vision priority: {}", visionPriorityId);

        List<Initiative> initiatives = initiativeRepository.findByVisionPriority(visionPriorityId, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByOwnerNode(Long ownerNodeId) {
        log.debug("Fetching initiatives for owner node: {}", ownerNodeId);

        List<Initiative> initiatives = initiativeRepository.findByOwnerNodeIdAndStatusCode(ownerNodeId, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getActiveInitiativesOnDate(LocalDate date) {
        log.debug("Fetching initiatives active on date: {}", date);

        List<Initiative> initiatives = initiativeRepository.findActiveInitiativesOnDate(date, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByTimelineRange(LocalDate fromDate, LocalDate toDate) {
        log.debug("Fetching initiatives by timeline range: {} to {}", fromDate, toDate);

        List<Initiative> initiatives = initiativeRepository.findInitiativesByTimelineRange(fromDate, toDate, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByBudgetUtilization(BigDecimal utilizationThreshold) {
        log.debug("Fetching initiatives by budget utilization threshold: {}", utilizationThreshold);

        List<Initiative> initiatives = initiativeRepository.findByBudgetUtilization(utilizationThreshold, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InitiativeResponseDto> getInitiativesByType(Integer type) {
        log.debug("Fetching initiatives by type: {}", type);

        List<Initiative> initiatives = initiativeRepository.findByTypeAndStatusCodeOrderByCreatedAtDesc(type, ACTIVE_STATUS);
        return initiatives.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getInitiativeSummaryByProgram(Long programId) {
        log.debug("Fetching initiative summary for program: {}", programId);

        return initiativeRepository.getInitiativeSummaryByProgram(programId, ACTIVE_STATUS);
    }

    public long countInitiativesByProgram(Long programId) {
        log.debug("Counting initiatives for program: {}", programId);

        return initiativeRepository.countByProgramId(programId, ACTIVE_STATUS);
    }

    public long countInitiativesByOwnerNode(Long ownerNodeId) {
        log.debug("Counting initiatives for owner node: {}", ownerNodeId);

        return initiativeRepository.countByOwnerNodeId(ownerNodeId, ACTIVE_STATUS);
    }

    // Helper method to convert Initiative entity to DTO
    private InitiativeResponseDto convertToResponseDto(Initiative initiative) {
        InitiativeResponseDto dto = InitiativeResponseDto.builder()
                .id(initiative.getId())
                .primaryName(initiative.getPrimaryName())
                .secondaryName(initiative.getSecondaryName())
                .primaryDescription(initiative.getPrimaryDescription())
                .secondaryDescription(initiative.getSecondaryDescription())
                .parentId(initiative.getParentId())
                .contributionPercent(initiative.getContributionPercent())
                .ownerId(initiative.getOwnerId())
                .plannedTotalBudget(initiative.getPlannedTotalBudget())
                .type(initiative.getType())
                .startDate(initiative.getStartDate())
                .endDate(initiative.getEndDate())
                .planningStatusCode(initiative.getPlanningStatusCode())
                .progressStatusCode(initiative.getProgressStatusCode())
                .calculatedProgressPercent(initiative.getCalculatedProgressPercent())
                .hybridProgressPercent(initiative.getHybridProgressPercent())
                .effectiveProgress(initiative.getEffectiveProgressPercent())
                .calculatedTotalBudget(initiative.getCalculatedTotalBudget())
                .calculatedTotalPayments(initiative.getCalculatedTotalPayments())
                .budgetUtilization(initiative.getBudgetUtilization())
                .ownerNodeId(initiative.getOwnerNodeId())
                .createdBy(initiative.getCreatedBy())
                .createdAt(initiative.getCreatedAt())
                .lastModifiedBy(initiative.getLastModifiedBy())
                .lastModifiedAt(initiative.getLastModifiedAt())
                .statusCode(initiative.getStatusCode())
                .build();

        // Calculate timeline status
        enrichWithTimelineStatus(dto, initiative);

        // Calculate aggregated data
        enrichWithAggregatedData(dto, initiative);

        return dto;
    }

    private void enrichWithTimelineStatus(InitiativeResponseDto dto, Initiative initiative) {
        LocalDate now = LocalDate.now();

        if (initiative.getStartDate() != null && initiative.getEndDate() != null) {
            boolean isActive = initiative.isWithinTimeline(now);
            dto.setIsActive(isActive);

            if (isActive) {
                long daysRemaining = ChronoUnit.DAYS.between(now, initiative.getEndDate());
                dto.setDaysRemaining(daysRemaining);
                dto.setTimelineStatus(daysRemaining > 30 ? "ON_TRACK" : "APPROACHING_DEADLINE");
            } else if (now.isBefore(initiative.getStartDate())) {
                dto.setTimelineStatus("FUTURE");
                dto.setDaysRemaining(ChronoUnit.DAYS.between(now, initiative.getStartDate()));
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

    private void enrichWithAggregatedData(InitiativeResponseDto dto, Initiative initiative) {
        // These would require additional repository calls
        dto.setProjectCount(0);
        dto.setActiveProjectCount(0);
        dto.setCompletedProjectCount(0);
        dto.setIndicatorCount(0);
        dto.setContributorCount(0);

        // TODO: Implement with additional queries
    }

    // Validation methods
    public boolean validateInitiativeTimeline(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !startDate.isAfter(endDate);
    }

    public boolean validateInitiativeContribution(Double contributionPercent) {
        return contributionPercent != null && contributionPercent >= 0.0 && contributionPercent <= 100.0;
    }

    public boolean validateInitiativeBudget(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}