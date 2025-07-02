package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.GoalResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Goal;
import com.project.Tadafur_api.domain.strategy.repository.GoalRepository;
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
public class GoalService {

    private final GoalRepository goalRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "goals", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<GoalResponseDto> getAllActiveGoals(Pageable pageable) {
        log.debug("Fetching active goals - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Goal> goals = goalRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return goals.map(this::convertToResponseDto);
    }

    @Cacheable(value = "goal-details", key = "#id")
    public GoalResponseDto getGoalById(Long id) {
        log.debug("Fetching goal by ID: {}", id);

        Goal goal = goalRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));

        return convertToResponseDto(goal);
    }

    public List<GoalResponseDto> getGoalsByPerspective(Long perspectiveId) {
        log.debug("Fetching goals for perspective: {}", perspectiveId);

        List<Goal> goals = goalRepository.findByParentIdAndStatusCodeOrderByCreatedAtDesc(perspectiveId, ACTIVE_STATUS);
        return goals.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<GoalResponseDto> getGoalsByOwner(Long ownerId) {
        log.debug("Fetching goals for owner: {}", ownerId);

        List<Goal> goals = goalRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return goals.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<GoalResponseDto> searchGoals(String query, Pageable pageable) {
        log.debug("Searching goals with query: '{}'", query);

        Page<Goal> goals = goalRepository.searchGoals(query, ACTIVE_STATUS, pageable);
        return goals.map(this::convertToResponseDto);
    }

    public List<GoalResponseDto> getActiveGoalsOnDate(LocalDate date) {
        log.debug("Fetching goals active on date: {}", date);

        List<Goal> goals = goalRepository.findActiveGoalsOnDate(date, ACTIVE_STATUS);
        return goals.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<GoalResponseDto> getGoalsByVisionPriority(Long visionPriority) {
        log.debug("Fetching goals for vision priority: {}", visionPriority);

        List<Goal> goals = goalRepository.findByVisionPriority(visionPriority, ACTIVE_STATUS);
        return goals.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<GoalResponseDto> getGoalsByMinimumProgress(BigDecimal minProgress) {
        log.debug("Fetching goals with minimum progress: {}", minProgress);

        List<Goal> goals = goalRepository.findByMinimumProgress(minProgress, ACTIVE_STATUS);
        return goals.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getGoalSummaryByPerspective(Long perspectiveId) {
        log.debug("Fetching goal summary for perspective: {}", perspectiveId);

        return goalRepository.getGoalSummaryByPerspective(perspectiveId, ACTIVE_STATUS);
    }

    // Helper method to convert Goal entity to DTO
    private GoalResponseDto convertToResponseDto(Goal goal) {
        GoalResponseDto dto = GoalResponseDto.builder()
                .id(goal.getId())
                .primaryName(goal.getPrimaryName())
                .secondaryName(goal.getSecondaryName())
                .primaryDescription(goal.getPrimaryDescription())
                .secondaryDescription(goal.getSecondaryDescription())
                .parentId(goal.getParentId())
                .ownerId(goal.getOwnerId())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .planningStatusCode(goal.getPlanningStatusCode())
                .progressStatusCode(goal.getProgressStatusCode())
                .calculatedProgressPercent(goal.getCalculatedProgressPercent())
                .hybridProgressPercent(goal.getHybridProgressPercent())
                .effectiveProgress(goal.getEffectiveProgressPercent())
                .visionPriority(goal.getVisionPriority())
                .plannedTotalBudget(goal.getPlannedTotalBudget())
                .calculatedTotalBudget(goal.getCalculatedTotalBudget())
                .calculatedTotalPayments(goal.getCalculatedTotalPayments())
                .budgetUtilization(goal.getBudgetUtilization())
                .createdBy(goal.getCreatedBy())
                .createdAt(goal.getCreatedAt())
                .lastModifiedBy(goal.getLastModifiedBy())
                .lastModifiedAt(goal.getLastModifiedAt())
                .statusCode(goal.getStatusCode())
                .build();

        // Calculate timeline status
        enrichWithTimelineStatus(dto, goal);

        // TODO: Add aggregated data (program count, etc.) - requires additional queries

        return dto;
    }

    private void enrichWithTimelineStatus(GoalResponseDto dto, Goal goal) {
        LocalDate now = LocalDate.now();

        if (goal.getStartDate() != null && goal.getEndDate() != null) {
            boolean isActive = goal.isWithinTimeline(now);
            dto.setIsActive(isActive);

            if (isActive) {
                long daysRemaining = ChronoUnit.DAYS.between(now, goal.getEndDate());
                dto.setDaysRemaining(daysRemaining);
                dto.setTimelineStatus(daysRemaining > 30 ? "ON_TRACK" : "APPROACHING_DEADLINE");
            } else if (now.isBefore(goal.getStartDate())) {
                dto.setTimelineStatus("FUTURE");
                dto.setDaysRemaining(ChronoUnit.DAYS.between(now, goal.getStartDate()));
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
}