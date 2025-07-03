package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.ProjectResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.Project;
import com.project.Tadafur_api.domain.strategy.repository.ProjectRepository;
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
public class ProjectService {

    private final ProjectRepository projectRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "projects", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ProjectResponseDto> getAllActiveProjects(Pageable pageable) {
        log.debug("Fetching active projects - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Project> projects = projectRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Cacheable(value = "project-details", key = "#id")
    public ProjectResponseDto getProjectById(Long id) {
        log.debug("Fetching project by ID: {}", id);

        Project project = projectRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        return convertToResponseDto(project);
    }

    public List<ProjectResponseDto> getProjectsByInitiative(Long initiativeId) {
        log.debug("Fetching projects for initiative: {}", initiativeId);

        List<Project> projects = projectRepository.findByParentIdAndStatusCodeOrderByCreatedAtDesc(initiativeId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByOwner(Long ownerId) {
        log.debug("Fetching projects for owner: {}", ownerId);

        List<Project> projects = projectRepository.findByOwnerIdAndStatusCodeOrderByCreatedAtDesc(ownerId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ProjectResponseDto> searchProjects(String query, Pageable pageable) {
        log.debug("Searching projects with query: '{}'", query);

        Page<Project> projects = projectRepository.searchProjects(query, ACTIVE_STATUS, pageable);
        return projects.map(this::convertToResponseDto);
    }

    public List<ProjectResponseDto> getActiveProjectsOnDate(LocalDate date) {
        log.debug("Fetching projects active on date: {}", date);

        List<Project> projects = projectRepository.findActiveProjectsOnDate(date, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByTimelineRange(LocalDate fromDate, LocalDate toDate) {
        log.debug("Fetching projects by timeline range: {} to {}", fromDate, toDate);

        List<Project> projects = projectRepository.findProjectsByTimelineRange(fromDate, toDate, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByMinimumProgress(BigDecimal minProgress) {
        log.debug("Fetching projects with minimum progress: {}", minProgress);

        List<Project> projects = projectRepository.findByMinimumProgress(minProgress, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByPriority(Long priorityId) {
        log.debug("Fetching projects by priority: {}", priorityId);

        List<Project> projects = projectRepository.findByPriorityIdAndStatusCodeOrderByCreatedAtDesc(priorityId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByStatus(Long statusId) {
        log.debug("Fetching projects by status: {}", statusId);

        List<Project> projects = projectRepository.findByStatusIdAndStatusCodeOrderByCreatedAtDesc(statusId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByMethodology(Long methodologyId) {
        log.debug("Fetching projects by methodology: {}", methodologyId);

        List<Project> projects = projectRepository.findByProjectMethodologyIdAndStatusCodeOrderByCreatedAtDesc(methodologyId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByVisionPriority(String visionPriorityId) {
        log.debug("Fetching projects by vision priority: {}", visionPriorityId);

        List<Project> projects = projectRepository.findByVisionPriority(visionPriorityId, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        log.debug("Fetching projects by budget range: {} to {}", minBudget, maxBudget);

        List<Project> projects = projectRepository.findProjectsByBudgetRange(minBudget, maxBudget, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getOverdueProjects() {
        log.debug("Fetching overdue projects");

        LocalDate currentDate = LocalDate.now();
        List<Project> projects = projectRepository.findOverdueProjects(currentDate, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> getProjectsByType(Integer type) {
        log.debug("Fetching projects by type: {}", type);

        List<Project> projects = projectRepository.findByTypeAndStatusCodeOrderByCreatedAtDesc(type, ACTIVE_STATUS);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getProjectSummaryByInitiative(Long initiativeId) {
        log.debug("Fetching project summary for initiative: {}", initiativeId);

        return projectRepository.getProjectSummaryByInitiative(initiativeId, ACTIVE_STATUS);
    }

    public long countProjectsByInitiative(Long initiativeId) {
        log.debug("Counting projects for initiative: {}", initiativeId);

        return projectRepository.countByInitiativeId(initiativeId, ACTIVE_STATUS);
    }

    public long countProjectsByOwner(Long ownerId) {
        log.debug("Counting projects for owner: {}", ownerId);

        return projectRepository.countByOwnerIdAndStatusCode(ownerId, ACTIVE_STATUS);
    }

    public long countProjectsByPriority(Long priorityId) {
        log.debug("Counting projects for priority: {}", priorityId);

        return projectRepository.countByPriorityId(priorityId, ACTIVE_STATUS);
    }

    // Helper method to convert Project entity to DTO
    private ProjectResponseDto convertToResponseDto(Project project) {
        ProjectResponseDto dto = ProjectResponseDto.builder()
                .id(project.getId())
                .primaryName(project.getPrimaryName())
                .secondaryName(project.getSecondaryName())
                .primaryDescription(project.getPrimaryDescription())
                .secondaryDescription(project.getSecondaryDescription())
                .parentId(project.getParentId())
                .contributionPercent(project.getContributionPercent())
                .ownerId(project.getOwnerId())
                .plannedTotalBudget(project.getPlannedTotalBudget())
                .type(project.getType())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .planningStatusCode(project.getPlanningStatusCode())
                .progressStatusCode(project.getProgressStatusCode())
                .actualCost(project.getActualCost())
                .priorityId(project.getPriorityId())
                .statusId(project.getStatusId())
                .projectMethodologyId(project.getProjectMethodologyId())
                .progressByEffort(project.getProgressByEffort())
                .progressByAverage(project.getProgressByAverage())
                .progressSpecificationId(project.getProgressSpecificationId())
                .propagationModelId(project.getPropagationModelId())
                .manualProgressByEffort(project.getManualProgressByEffort())
                .manualProgressByAverage(project.getManualProgressByAverage())
                .calculatedProgressPercent(project.getCalculatedProgressPercent())
                .hybridProgressPercent(project.getHybridProgressPercent())
                .effectiveProgress(project.getEffectiveProgressPercent())
                .calculatedTotalBudget(project.getCalculatedTotalBudget())
                .calculatedTotalPayments(project.getCalculatedTotalPayments())
                .budgetUtilization(project.getBudgetUtilization())
                .summarySentDate(project.getSummarySentDate())
                .summaryPeriod(project.getSummaryPeriod())
                .createdBy(project.getCreatedBy())
                .createdAt(project.getCreatedAt())
                .lastModifiedBy(project.getLastModifiedBy())
                .lastModifiedAt(project.getLastModifiedAt())
                .statusCode(project.getStatusCode())
                .build();

        // Calculate timeline status
        enrichWithTimelineStatus(dto, project);

        // Calculate aggregated data
        enrichWithAggregatedData(dto, project);

        return dto;
    }

    private void enrichWithTimelineStatus(ProjectResponseDto dto, Project project) {
        LocalDate now = LocalDate.now();

        if (project.getStartDate() != null && project.getEndDate() != null) {
            boolean isActive = project.isWithinTimeline(now);
            dto.setIsActive(isActive);

            if (isActive) {
                long daysRemaining = ChronoUnit.DAYS.between(now, project.getEndDate());
                dto.setDaysRemaining(daysRemaining);
                dto.setTimelineStatus(daysRemaining > 30 ? "ON_TRACK" : "APPROACHING_DEADLINE");
            } else if (now.isBefore(project.getStartDate())) {
                dto.setTimelineStatus("FUTURE");
                dto.setDaysRemaining(ChronoUnit.DAYS.between(now, project.getStartDate()));
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

    private void enrichWithAggregatedData(ProjectResponseDto dto, Project project) {
        // These would require additional repository calls
        dto.setWorkItemCount(0);
        dto.setCompletedWorkItemCount(0);
        dto.setMemberCount(0);
        dto.setStakeholderCount(0);

        // TODO: Implement with additional queries
    }

    // Validation methods
    public boolean validateProjectTimeline(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !startDate.isAfter(endDate);
    }

    public boolean validateProjectContribution(Double contributionPercent) {
        return contributionPercent != null && contributionPercent >= 0.0 && contributionPercent <= 100.0;
    }

    public boolean validateProjectBudget(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}