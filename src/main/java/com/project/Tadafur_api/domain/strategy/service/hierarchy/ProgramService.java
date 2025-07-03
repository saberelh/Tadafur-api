package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.ProgramResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Program;
import com.project.Tadafur_api.domain.strategy.repository.ProgramRepository;
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
public class ProgramService {

    private final ProgramRepository programRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "programs", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ProgramResponseDto> getAllActivePrograms(Pageable pageable) {
        log.debug("Fetching active programs - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Program> programs = programRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return programs.map(this::convertToResponseDto);
    }

    @Cacheable(value = "program-details", key = "#id")
    public ProgramResponseDto getProgramById(Long id) {
        log.debug("Fetching program by ID: {}", id);

        Program program = programRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "id", id));

        return convertToResponseDto(program);
    }

    public List<ProgramResponseDto> getProgramsByGoal(Long goalId) {
        log.debug("Fetching programs for goal: {}", goalId);

        List<Program> programs = programRepository.findByParentIdAndStatusCodeOrderByCreatedAtDesc(goalId, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProgramResponseDto> getProgramsByOwner(Long ownerId) {
        log.debug("Fetching programs for owner: {}", ownerId);

        List<Program> programs = programRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ProgramResponseDto> searchPrograms(String query, Pageable pageable) {
        log.debug("Searching programs with query: '{}'", query);

        Page<Program> programs = programRepository.searchPrograms(query, ACTIVE_STATUS, pageable);
        return programs.map(this::convertToResponseDto);
    }

    public List<ProgramResponseDto> getProgramsByMinimumProgress(BigDecimal minProgress) {
        log.debug("Fetching programs with minimum progress: {}", minProgress);

        List<Program> programs = programRepository.findByMinimumProgress(minProgress, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProgramResponseDto> getProgramsByVisionPriority(String visionPriorityId) {
        log.debug("Fetching programs for vision priority: {}", visionPriorityId);

        List<Program> programs = programRepository.findByVisionPriority(visionPriorityId, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProgramResponseDto> getProgramsByMinimumContribution(Double minContribution) {
        log.debug("Fetching programs with minimum contribution: {}", minContribution);

        List<Program> programs = programRepository.findByMinimumContribution(minContribution, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProgramResponseDto> getProgramsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        log.debug("Fetching programs by budget range: {} to {}", minBudget, maxBudget);

        List<Program> programs = programRepository.findProgramsByBudgetRange(minBudget, maxBudget, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProgramResponseDto> getProgramsByPlanningStatus(String planningStatus) {
        log.debug("Fetching programs by planning status: {}", planningStatus);

        List<Program> programs = programRepository.findByPlanningStatus(planningStatus, ACTIVE_STATUS);
        return programs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getProgramSummaryByGoal(Long goalId) {
        log.debug("Fetching program summary for goal: {}", goalId);

        return programRepository.getProgramSummaryByGoal(goalId, ACTIVE_STATUS);
    }

    public long countProgramsByGoal(Long goalId) {
        log.debug("Counting programs for goal: {}", goalId);

        return programRepository.countByGoalId(goalId, ACTIVE_STATUS);
    }

    public long countProgramsByOwner(Long ownerId) {
        log.debug("Counting programs for owner: {}", ownerId);

        return programRepository.countByOwnerIdAndStatusCode(ownerId, ACTIVE_STATUS);
    }

    // Helper method to convert Program entity to DTO
    private ProgramResponseDto convertToResponseDto(Program program) {
        ProgramResponseDto dto = ProgramResponseDto.builder()
                .id(program.getId())
                .primaryName(program.getPrimaryName())
                .secondaryName(program.getSecondaryName())
                .primaryDescription(program.getPrimaryDescription())
                .secondaryDescription(program.getSecondaryDescription())
                .parentId(program.getParentId())
                .contributionPercent(program.getContributionPercent())
                .ownerId(program.getOwnerId())
                .planningStatusCode(program.getPlanningStatusCode())
                .progressStatusCode(program.getProgressStatusCode())
                .calculatedProgressPercent(program.getCalculatedProgressPercent())
                .hybridProgressPercent(program.getHybridProgressPercent())
                .effectiveProgress(program.getEffectiveProgressPercent())
                .plannedTotalBudget(program.getPlannedTotalBudget())
                .calculatedTotalBudget(program.getCalculatedTotalBudget())
                .calculatedTotalPayments(program.getCalculatedTotalPayments())
                .budgetUtilization(program.getBudgetUtilization())
                .createdBy(program.getCreatedBy())
                .createdAt(program.getCreatedAt())
                .lastModifiedBy(program.getLastModifiedBy())
                .lastModifiedAt(program.getLastModifiedAt())
                .statusCode(program.getStatusCode())
                .build();

        // Calculate aggregated data
        enrichWithAggregatedData(dto, program);

        return dto;
    }

    private void enrichWithAggregatedData(ProgramResponseDto dto, Program program) {
        // These would require additional repository calls
        dto.setInitiativeCount(0);
        dto.setTotalProjectCount(0);
        dto.setIndicatorCount(0);
        dto.setContributorCount(0);

        // TODO: Implement with additional queries
    }

    // Validation methods
    public boolean validateProgramContribution(Double contributionPercent) {
        return contributionPercent != null && contributionPercent >= 0.0 && contributionPercent <= 100.0;
    }

    public boolean validateProgramBudget(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}