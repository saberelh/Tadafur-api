package com.project.Tadafur_api.application.service.analytics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Tadafur_api.application.dto.analytics.PaymentTransactionDto;
import com.project.Tadafur_api.application.dto.analytics.ProjectSpendingDetailsDto;
import com.project.Tadafur_api.application.dto.analytics.SpendingSummaryDto;
import com.project.Tadafur_api.application.dto.analytics.StrategicHealthDto;
import com.project.Tadafur_api.domain.strategy.entity.*;
import com.project.Tadafur_api.domain.strategy.repository.*;
import com.project.Tadafur_api.domain.strategy.repository.ProjectRepository.ProjectPaymentDetails;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnalyticsService {

    private final StrategyRepository strategyRepository;
    private final PerspectiveRepository perspectiveRepository;
    private final GoalRepository goalRepository;
    private final ProgramRepository programRepository;
    private final InitiativeRepository initiativeRepository;
    private final ProjectRepository projectRepository;
    private final BudgetPaymentRepository budgetPaymentRepository;

    private static final String DEFAULT_LANG = "en";

    // --- High-Performance Spending Details API Methods (UNCHANGED) ---

    public List<ProjectSpendingDetailsDto> getSpendingDetails(Long projectId, Long ownerId, String lang) {
        log.info("Fetching spending details efficiently for projectId: [{}] and ownerId: [{}]", projectId, ownerId);
        List<ProjectPaymentDetails> flatResults = projectRepository.getProjectPaymentDetails(projectId, ownerId);

        Map<Long, List<ProjectPaymentDetails>> groupedByProject = flatResults.stream()
                .collect(Collectors.groupingBy(ProjectPaymentDetails::getProjectId));

        return groupedByProject.values().stream()
                .map(projectData -> buildDetailsForProject(projectData, lang))
                .collect(Collectors.toList());
    }

    private ProjectSpendingDetailsDto buildDetailsForProject(List<ProjectPaymentDetails> projectData, String lang) {
        ProjectPaymentDetails firstRecord = projectData.get(0);

        List<PaymentTransactionDto> paymentDtos = projectData.stream()
                .filter(p -> p.getPaymentId() != null)
                .map(p -> PaymentTransactionDto.builder()
                        .id(p.getPaymentId())
                        .paymentDate(p.getPaymentDate())
                        .amount(p.getPaymentAmount())
                        .paymentNotes(parseJsonMap(p.getNotesTranslations()))
                        .createdBy(p.getPaymentCreatedBy())
                        .createdAt(p.getPaymentCreatedAt())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPayments = paymentDtos.stream()
                .map(PaymentTransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SpendingSummaryDto summary = SpendingSummaryDto.builder()
                .plannedTotalBudget(firstRecord.getPlannedTotalBudget())
                .paymentBudget(totalPayments)
                .build();

        return ProjectSpendingDetailsDto.builder()
                .projectId(firstRecord.getProjectId())
                .projectName(getTranslatedValue(parseJsonMap(firstRecord.getNameTranslations()), lang, "Unknown Project"))
                .summary(summary)
                .payments(paymentDtos)
                .build();
    }

    private Map<String, String> parseJsonMap(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            log.error("Error parsing JSONB string from database: {}", json, e);
            return Collections.emptyMap();
        }
    }

    // --- REFACTORED: High-Performance Strategic Health Methods ---

    /**
     * REFACTORED: Gets the strategic health for ALL strategies using an efficient in-memory approach.
     */
    public List<StrategicHealthDto> getAllStrategicHealths(String dateRange, String lang) {
        log.info("Calculating strategic health for all strategies using in-memory processing.");

        // Step 1: Fetch all hierarchy items from the database in simple, fast batches.
        List<Strategy> allStrategies = strategyRepository.findAll();
        Map<Long, List<Perspective>> perspectivesByParent = perspectiveRepository.findAll().stream().collect(Collectors.groupingBy(Perspective::getParentId));
        Map<Long, List<Goal>> goalsByParent = goalRepository.findAll().stream().collect(Collectors.groupingBy(Goal::getParentId));
        Map<Long, List<Program>> programsByParent = programRepository.findAll().stream().collect(Collectors.groupingBy(Program::getParentId));
        Map<Long, List<Initiative>> initiativesByParent = initiativeRepository.findAll().stream().collect(Collectors.groupingBy(Initiative::getParentId));
        Map<Long, List<Project>> projectsByParent = projectRepository.findAll().stream().collect(Collectors.groupingBy(Project::getParentId));

        // Step 2: For each strategy, build its health DTO using the pre-fetched in-memory data.
        return allStrategies.stream()
                .map(strategy -> {
                    List<Project> projectsForStrategy = getProjectsForStrategyInMemory(strategy.getId(), perspectivesByParent, goalsByParent, programsByParent, initiativesByParent, projectsByParent);
                    return buildHealthDtoFromProjects(strategy, projectsForStrategy, lang);
                })
                .collect(Collectors.toList());
    }

    /**
     * UNCHANGED: The original N+1 query approach is acceptable and often efficient enough for a SINGLE strategy lookup.
     */
    public StrategicHealthDto getStrategicHealth(Long strategyId, String dateRange, String lang) {
        log.info("Calculating strategic health for single Strategy ID: {}", strategyId);
        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", strategyId));

        // This helper method performs the hierarchy traversal for a single strategy.
        List<Project> projectsForStrategy = findProjectsForSingleStrategy(strategyId);

        return buildHealthDtoFromProjects(strategy, projectsForStrategy, lang);
    }

    /**
     * NEW HELPER: Traverses the in-memory maps to find all projects for a given strategy ID.
     * This is used by the `getAllStrategicHealths` method for high performance.
     */
    private List<Project> getProjectsForStrategyInMemory(
            Long strategyId,
            Map<Long, List<Perspective>> perspectives,
            Map<Long, List<Goal>> goals,
            Map<Long, List<Program>> programs,
            Map<Long, List<Initiative>> initiatives,
            Map<Long, List<Project>> projects
    ) {
        List<Project> result = new ArrayList<>();
        for (Perspective p : perspectives.getOrDefault(strategyId, Collections.emptyList())) {
            for (Goal g : goals.getOrDefault(p.getId(), Collections.emptyList())) {
                for (Program prog : programs.getOrDefault(g.getId(), Collections.emptyList())) {
                    for (Initiative i : initiatives.getOrDefault(prog.getId(), Collections.emptyList())) {
                        result.addAll(projects.getOrDefault(i.getId(), Collections.emptyList()));
                    }
                }
            }
        }
        return result;
    }

    /**
     * NEW HELPER: Contains the logic to build the health DTO, shared by both public methods.
     */
    private StrategicHealthDto buildHealthDtoFromProjects(Strategy strategy, List<Project> projects, String lang) {
        if (projects.isEmpty()) {
            return StrategicHealthDto.builder()
                    .strategyId(strategy.getId())
                    .strategyName(getTranslatedValue(strategy.getNameTranslations(), lang, "Unknown Strategy"))
                    .healthStatus("No Data")
                    .build();
        }

        BigDecimal overallProgress = calculateOverallProgress(projects);
        BigDecimal budgetVariance = calculateBudgetVariance(projects);
        Double scheduleVariance = calculateScheduleVariance(projects);

        boolean hasCriticalError = projects.stream()
                .anyMatch(p -> (p.getPlanningStatusCode() != null && !"OK".equalsIgnoreCase(p.getPlanningStatusCode())) ||
                        (p.getProgressStatusCode() != null && !"OK".equalsIgnoreCase(p.getProgressStatusCode())));

        String healthStatus = "On-Track";
        if (hasCriticalError) {
            healthStatus = "Off-Track";
        } else if (overallProgress.compareTo(new BigDecimal("50")) < 0 || budgetVariance.compareTo(BigDecimal.ZERO) < 0 || scheduleVariance > 15) {
            healthStatus = "At-Risk";
        }

        return StrategicHealthDto.builder()
                .strategyId(strategy.getId())
                .strategyName(getTranslatedValue(strategy.getNameTranslations(), lang, "Unknown Strategy"))
                .overallProgress(overallProgress)
                .budgetVariance(budgetVariance)
                .scheduleVarianceDays(scheduleVariance)
                .activeRisks(0)
                .healthStatus(healthStatus)
                .build();
    }

    private List<Project> findProjectsForSingleStrategy(Long strategyId) {
        List<Project> projectsForStrategy = new ArrayList<>();
        List<Perspective> perspectives = perspectiveRepository.findByParentId(strategyId);
        for (Perspective p : perspectives) {
            List<Goal> goals = goalRepository.findByParentId(p.getId());
            for (Goal g : goals) {
                List<Program> programs = programRepository.findByParentId(g.getId());
                for (Program prog : programs) {
                    List<Initiative> initiatives = initiativeRepository.findByParentId(prog.getId());
                    for (Initiative i : initiatives) {
                        projectsForStrategy.addAll(projectRepository.findByParentId(i.getId()));
                    }
                }
            }
        }
        return projectsForStrategy;
    }

    private BigDecimal calculateOverallProgress(List<Project> projects) {
        if (projects.isEmpty()) return BigDecimal.ZERO;
        BigDecimal totalProgress = projects.stream()
                .map(p -> p.getCalculatedProgressPercent() != null ? p.getCalculatedProgressPercent() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalProgress.divide(new BigDecimal(projects.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateBudgetVariance(List<Project> projects) {
        BigDecimal totalPlanned = projects.stream()
                .map(p -> p.getPlannedTotalBudget() != null ? p.getPlannedTotalBudget() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalActual = projects.stream()
                .map(p -> p.getActualCost() != null ? p.getActualCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPlanned.subtract(totalActual);
    }

    private Double calculateScheduleVariance(List<Project> projects) {
        LocalDate today = LocalDate.now();
        List<Project> delayedProjects = projects.stream()
                .filter(p -> p.getProgressStatusCode() != null && !"COMPLETED".equalsIgnoreCase(p.getProgressStatusCode()))
                .filter(p -> p.getEndDate() != null && today.isAfter(p.getEndDate()))
                .toList();

        if (delayedProjects.isEmpty()) return 0.0;

        long totalDelayDays = delayedProjects.stream()
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getEndDate(), today))
                .sum();

        return (double) totalDelayDays / delayedProjects.size();
    }

    private String getTranslatedValue(Map<String, String> translations, String lang, String fallback) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get("en")))
                .orElse(fallback);
    }
}