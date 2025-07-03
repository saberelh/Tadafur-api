package com.project.Tadafur_api.domain.strategy.service.hierarchy;

import com.project.Tadafur_api.domain.strategy.dto.response.WorkItemResponseDto;
import com.project.Tadafur_api.domain.strategy.entity.WorkItem;
import com.project.Tadafur_api.domain.strategy.repository.WorkItemRepository;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WorkItemService {

    private final WorkItemRepository workItemRepository;

    private static final String ACTIVE_STATUS = "ACTIVE";

    @Cacheable(value = "work-items", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<WorkItemResponseDto> getAllActiveWorkItems(Pageable pageable) {
        log.debug("Fetching active work items - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<WorkItem> workItems = workItemRepository.findByStatusCodeOrderByCreatedAtDesc(ACTIVE_STATUS, pageable);
        return workItems.map(this::convertToResponseDto);
    }

    @Cacheable(value = "work-item-details", key = "#id")
    public WorkItemResponseDto getWorkItemById(Long id) {
        log.debug("Fetching work item by ID: {}", id);

        WorkItem workItem = workItemRepository.findByIdAndStatusCode(id, ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException("WorkItem", "id", id));

        return convertToResponseDto(workItem);
    }

    public List<WorkItemResponseDto> getWorkItemsByProject(Long projectId) {
        log.debug("Fetching work items for project: {}", projectId);

        List<WorkItem> workItems = workItemRepository.findByProjectIdAndStatusCodeOrderByItemSortAsc(projectId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<WorkItemResponseDto> getWorkItemsByProjectPaginated(Long projectId, Pageable pageable) {
        log.debug("Fetching work items for project: {} with pagination", projectId);

        Page<WorkItem> workItems = workItemRepository.findByProjectIdAndStatusCodeOrderByItemSortAsc(projectId, ACTIVE_STATUS, pageable);
        return workItems.map(this::convertToResponseDto);
    }

    public List<WorkItemResponseDto> getSubTasks(Long parentId) {
        log.debug("Fetching subtasks for work item: {}", parentId);

        List<WorkItem> workItems = workItemRepository.findByParentIdAndStatusCodeOrderByItemSortAsc(parentId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsByAssignee(Long assigneeUserId) {
        log.debug("Fetching work items for assignee: {}", assigneeUserId);

        List<WorkItem> workItems = workItemRepository.findByAssigneeUserIdAndStatusCodeOrderByPlannedDueDateAsc(assigneeUserId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<WorkItemResponseDto> getWorkItemsByAssigneePaginated(Long assigneeUserId, Pageable pageable) {
        log.debug("Fetching work items for assignee: {} with pagination", assigneeUserId);

        Page<WorkItem> workItems = workItemRepository.findByAssigneeUserIdAndStatusCodeOrderByPlannedDueDateAsc(assigneeUserId, ACTIVE_STATUS, pageable);
        return workItems.map(this::convertToResponseDto);
    }

    public Page<WorkItemResponseDto> searchWorkItems(String query, Pageable pageable) {
        log.debug("Searching work items with query: '{}'", query);

        Page<WorkItem> workItems = workItemRepository.searchWorkItems(query, ACTIVE_STATUS, pageable);
        return workItems.map(this::convertToResponseDto);
    }

    public List<WorkItemResponseDto> getWorkItemsByPriority(Long priorityId) {
        log.debug("Fetching work items by priority: {}", priorityId);

        List<WorkItem> workItems = workItemRepository.findByPriorityIdAndStatusCodeOrderByPlannedDueDateAsc(priorityId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsByStatus(Long statusId) {
        log.debug("Fetching work items by status: {}", statusId);

        List<WorkItem> workItems = workItemRepository.findByStatusIdAndStatusCodeOrderByPlannedDueDateAsc(statusId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsDueOnDate(LocalDate date) {
        log.debug("Fetching work items due on date: {}", date);

        List<WorkItem> workItems = workItemRepository.findWorkItemsDueOnDate(date, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsDueInRange(LocalDate fromDate, LocalDate toDate) {
        log.debug("Fetching work items due between: {} and {}", fromDate, toDate);

        List<WorkItem> workItems = workItemRepository.findWorkItemsDueInRange(fromDate, toDate, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getOverdueWorkItems() {
        log.debug("Fetching overdue work items");

        LocalDate currentDate = LocalDate.now();
        List<WorkItem> workItems = workItemRepository.findOverdueWorkItems(currentDate, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsByMinimumProgress(BigDecimal minProgress) {
        log.debug("Fetching work items with minimum progress: {}", minProgress);

        List<WorkItem> workItems = workItemRepository.findByMinimumProgress(minProgress, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getCompletedWorkItems() {
        log.debug("Fetching completed work items");

        List<WorkItem> workItems = workItemRepository.findCompletedWorkItems(ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsByGroup(Long groupId) {
        log.debug("Fetching work items by group: {}", groupId);

        List<WorkItem> workItems = workItemRepository.findByWorkItemGroupIdAndStatusCodeOrderByItemSortAsc(groupId, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<WorkItemResponseDto> getWorkItemsByLevel(Long projectId, Integer level) {
        log.debug("Fetching work items for project: {} at level: {}", projectId, level);

        List<WorkItem> workItems = workItemRepository.findByProjectIdAndLevelAndStatusCodeOrderByItemSortAsc(projectId, level, ACTIVE_STATUS);
        return workItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getWorkItemSummaryByProject(Long projectId) {
        log.debug("Fetching work item summary for project: {}", projectId);

        return workItemRepository.getWorkItemSummaryByProject(projectId, ACTIVE_STATUS);
    }

    public Map<String, Object> getWorkItemSummaryByAssignee(Long assigneeUserId) {
        log.debug("Fetching work item summary for assignee: {}", assigneeUserId);

        return workItemRepository.getWorkItemSummaryByAssignee(assigneeUserId, ACTIVE_STATUS);
    }

    public long countWorkItemsByProject(Long projectId) {
        log.debug("Counting work items for project: {}", projectId);

        return workItemRepository.countByProjectId(projectId, ACTIVE_STATUS);
    }

    public long countWorkItemsByAssignee(Long assigneeUserId) {
        log.debug("Counting work items for assignee: {}", assigneeUserId);

        return workItemRepository.countByAssigneeUserId(assigneeUserId, ACTIVE_STATUS);
    }

    public long countSubTasks(Long parentId) {
        log.debug("Counting subtasks for work item: {}", parentId);

        return workItemRepository.countByParentId(parentId, ACTIVE_STATUS);
    }

    // Helper method to convert WorkItem entity to DTO
    private WorkItemResponseDto convertToResponseDto(WorkItem workItem) {
        WorkItemResponseDto dto = WorkItemResponseDto.builder()
                .id(workItem.getId())
                .projectId(workItem.getProjectId())
                .parentId(workItem.getParentId())
                .primaryName(workItem.getPrimaryName())
                .secondaryName(workItem.getSecondaryName())
                .primaryDescription(workItem.getPrimaryDescription())
                .secondaryDescription(workItem.getSecondaryDescription())
                .priorityId(workItem.getPriorityId())
                .statusId(workItem.getStatusId())
                .assigneeUserId(workItem.getAssigneeUserId())
                .estimatedTime(workItem.getEstimatedTime())
                .estimatedTimeUnit(workItem.getEstimatedTimeUnit())
                .actualTime(workItem.getActualTime())
                .actualTimeUnit(workItem.getActualTimeUnit())
                .plannedStartDate(workItem.getPlannedStartDate())
                .plannedDueDate(workItem.getPlannedDueDate())
                .actualStartDate(workItem.getActualStartDate())
                .actualDueDate(workItem.getActualDueDate())
                .progressPercent(workItem.getProgressPercent())
                .workItemGroupId(workItem.getWorkItemGroupId())
                .level(workItem.getLevel())
                .itemSort(workItem.getItemSort())
                .verificationResult(workItem.getVerificationResult())
                .progressByEffort(workItem.getProgressByEffort())
                .progressByAverage(workItem.getProgressByAverage())
                .manualProgressByEffort(workItem.getManualProgressByEffort())
                .manualProgressByAverage(workItem.getManualProgressByAverage())
                .effectiveProgress(workItem.getEffectiveProgress())
                .isAddedFromCustom(workItem.getIsAddedFromCustom())
                .isCompleted(workItem.isCompleted())
                .isOverdue(workItem.isOverdue())
                .createdBy(workItem.getCreatedBy())
                .createdAt(workItem.getCreatedAt())
                .lastModifiedBy(workItem.getLastModifiedBy())
                .lastModifiedAt(workItem.getLastModifiedAt())
                .statusCode(workItem.getStatusCode())
                .build();

        // Calculate aggregated data
        enrichWithAggregatedData(dto, workItem);

        return dto;
    }

    private void enrichWithAggregatedData(WorkItemResponseDto dto, WorkItem workItem) {
        // These would require additional repository calls
        dto.setSubTaskCount(0);
        dto.setCompletedSubTaskCount(0);
        dto.setAttachmentCount(0);
        dto.setDependencyCount(0);

        // TODO: Implement with additional queries
    }

    // Validation methods
    public boolean validateWorkItemDates(LocalDate plannedStartDate, LocalDate plannedDueDate) {
        if (plannedStartDate == null || plannedDueDate == null) {
            return false;
        }
        return !plannedStartDate.isAfter(plannedDueDate);
    }

    public boolean validateWorkItemProgress(BigDecimal progress) {
        return progress != null && progress.compareTo(BigDecimal.ZERO) >= 0 && progress.compareTo(BigDecimal.valueOf(100)) <= 0;
    }

    public boolean validateWorkItemTime(BigDecimal time) {
        return time == null || time.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean validateWorkItemLevel(Integer level) {
        return level != null && level >= 0 && level <= 10; // Assuming max 10 levels
    }
}