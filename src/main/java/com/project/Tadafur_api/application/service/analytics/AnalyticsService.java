package com.project.Tadafur_api.application.service.analytics;

import com.project.Tadafur_api.application.dto.analytics.PaymentTransactionDto;
import com.project.Tadafur_api.application.dto.analytics.ProjectSpendingDetailsDto;
import com.project.Tadafur_api.application.dto.analytics.SpendingSummaryDto;
import com.project.Tadafur_api.application.dto.analytics.StrategicHealthDto;
import com.project.Tadafur_api.domain.strategy.entity.*;
import com.project.Tadafur_api.domain.strategy.repository.*;
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
    private final BudgetPaymentRepository budgetPaymentRepository; // Inject the new repository

    private static final String DEFAULT_LANG = "en";

    // --- NEW: Spending Details API Methods ---

    public List<ProjectSpendingDetailsDto> getSpendingDetails(Long projectId, Long ownerId, String lang) {
        log.info("Fetching spending details for projectId: [{}] and ownerId: [{}]", projectId, ownerId);

        List<Project> projectsToProcess;

        if (projectId != null) {
            projectsToProcess = projectRepository.findById(projectId).map(Collections::singletonList).orElse(Collections.emptyList());
        } else if (ownerId != null) {
            projectsToProcess = projectRepository.findByOwnerId(ownerId);
        } else {
            projectsToProcess = projectRepository.findAll();
        }

        return projectsToProcess.stream()
                .map(project -> buildDetailsForProject(project, lang))
                .collect(Collectors.toList());
    }

    private ProjectSpendingDetailsDto buildDetailsForProject(Project project, String lang) {
        // Use the correct repository to find payments
        List<BudgetPayment> payments = budgetPaymentRepository.findByEntityIdAndEntityCode(project.getId(), "PROJECT");

        List<PaymentTransactionDto> paymentDtos = payments.stream()
                .map(p -> PaymentTransactionDto.builder()
                        .id(p.getId())
                        .paymentDate(p.getPaymentDate())
                        .amount(p.getAmount())
                        .englishPaymentNotes(p.getPrimaryPaymentNotes())
                        .arabicPaymentNotes(p.getSecondaryPaymentNotes())
                        .createdBy(p.getCreatedBy())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPayments = projectRepository.getSumOfPaymentsForProject(project.getId());

        SpendingSummaryDto summary = SpendingSummaryDto.builder()
                .plannedTotalBudget(project.getPlannedTotalBudget())
                .paymentBudget(totalPayments)
                .build();

        return ProjectSpendingDetailsDto.builder()
                .projectId(project.getId())
                .projectName(getTranslatedValue(project.getNameTranslations(), lang, "Unknown Project"))
                .summary(summary)
                .payments(paymentDtos)
                .build();
    }

    // --- UNCHANGED: Strategic Health Methods ---

    public List<StrategicHealthDto> getAllStrategicHealths(String dateRange, String lang) {
        log.info("Calculating strategic health for all strategies.");
        List<Strategy> allStrategies = strategyRepository.findAll();
        return allStrategies.stream()
                .map(strategy -> getStrategicHealth(strategy.getId(), dateRange, lang))
                .collect(Collectors.toList());
    }

    public StrategicHealthDto getStrategicHealth(Long strategyId, String dateRange, String lang) {
        log.info("Calculating strategic health for Strategy ID: {}", strategyId);
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);
        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", strategyId));
        HierarchyContainer hierarchy = findAllEntitiesForStrategy(strategyId);
        if (hierarchy.getProjects().isEmpty()) {
            return StrategicHealthDto.builder()
                    .strategyId(strategyId)
                    .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode, "Unknown Strategy"))
                    .healthStatus("No Data")
                    .build();
        }
        BigDecimal overallProgress = calculateOverallProgress(hierarchy.getProjects());
        BigDecimal budgetVariance = calculateBudgetVariance(hierarchy.getProjects());
        Double scheduleVariance = calculateScheduleVariance(hierarchy.getProjects());
        String healthStatus = determineHealthStatus(hierarchy, overallProgress, budgetVariance, scheduleVariance);
        return StrategicHealthDto.builder()
                .strategyId(strategyId)
                .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode, "Unknown Strategy"))
                .overallProgress(overallProgress)
                .budgetVariance(budgetVariance)
                .scheduleVarianceDays(scheduleVariance)
                .activeRisks(0)
                .healthStatus(healthStatus)
                .build();
    }

    private HierarchyContainer findAllEntitiesForStrategy(Long strategyId) {
        HierarchyContainer container = new HierarchyContainer();
        container.perspectives = perspectiveRepository.findByParentId(strategyId);
        for (Perspective p : container.perspectives) {
            List<Goal> goals = goalRepository.findByParentId(p.getId());
            container.goals.addAll(goals);
            for (Goal g : goals) {
                List<Program> programs = programRepository.findByParentId(g.getId());
                container.programs.addAll(programs);
                for (Program prog : programs) {
                    List<Initiative> initiatives = initiativeRepository.findByParentId(prog.getId());
                    container.initiatives.addAll(initiatives);
                    for (Initiative i : initiatives) {
                        List<Project> projects = projectRepository.findByParentId(i.getId());
                        container.projects.addAll(projects);
                    }
                }
            }
        }
        return container;
    }

    private String determineHealthStatus(HierarchyContainer hierarchy, BigDecimal progress, BigDecimal budgetVariance, Double scheduleVariance) {
        boolean hasCriticalError =
                hierarchy.getPerspectives().stream().anyMatch(e -> !"OK".equalsIgnoreCase(e.getPlanningStatusCode())) ||
                        hierarchy.getGoals().stream().anyMatch(e -> !"OK".equalsIgnoreCase(e.getPlanningStatusCode()) || !"OK".equalsIgnoreCase(e.getProgressStatusCode())) ||
                        hierarchy.getPrograms().stream().anyMatch(e -> !"OK".equalsIgnoreCase(e.getPlanningStatusCode()) || !"OK".equalsIgnoreCase(e.getProgressStatusCode())) ||
                        hierarchy.getInitiatives().stream().anyMatch(e -> !"OK".equalsIgnoreCase(e.getPlanningStatusCode()) || !"OK".equalsIgnoreCase(e.getProgressStatusCode())) ||
                        hierarchy.getProjects().stream().anyMatch(e -> !"OK".equalsIgnoreCase(e.getPlanningStatusCode()) || !"OK".equalsIgnoreCase(e.getProgressStatusCode()));
        if (hasCriticalError) return "Off-Track";
        if (progress.compareTo(new BigDecimal("75")) >= 0 && budgetVariance.compareTo(BigDecimal.ZERO) >= 0 && scheduleVariance <= 0) return "On-Track";
        if (progress.compareTo(new BigDecimal("50")) < 0 || budgetVariance.compareTo(BigDecimal.ZERO) < 0 || scheduleVariance > 15) return "At-Risk";
        return "On-Track";
    }

    private static class HierarchyContainer {
        List<Perspective> perspectives = new ArrayList<>();
        List<Goal> goals = new ArrayList<>();
        List<Program> programs = new ArrayList<>();
        List<Initiative> initiatives = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        public List<Perspective> getPerspectives() { return perspectives; }
        public List<Goal> getGoals() { return goals; }
        public List<Program> getPrograms() { return programs; }
        public List<Initiative> getInitiatives() { return initiatives; }
        public List<Project> getProjects() { return projects; }
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
        long delayedProjectsCount = projects.stream()
                .filter(p -> p.getProgressStatusCode() != null && !"COMPLETED".equalsIgnoreCase(p.getProgressStatusCode()))
                .filter(p -> p.getEndDate() != null && today.isAfter(p.getEndDate()))
                .count();
        if (delayedProjectsCount == 0) return 0.0;
        long totalDelayDays = projects.stream()
                .filter(p -> p.getProgressStatusCode() != null && !"COMPLETED".equalsIgnoreCase(p.getProgressStatusCode()))
                .filter(p -> p.getEndDate() != null && today.isAfter(p.getEndDate()))
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getEndDate(), today))
                .sum();
        return (double) totalDelayDays / delayedProjectsCount;
    }

    private String getTranslatedValue(Map<String, String> translations, String lang, String fallback) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get("en")))
                .orElse(fallback);
    }
}