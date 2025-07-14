// =================================================================================
// STEP 1: UPDATE THE ANALYTICS SERVICE
// We will add a new method to loop through all strategies and calculate the health for each one.
// =================================================================================

// File: src/main/java/com/project/Tadafur_api/application/service/analytics/AnalyticsService.java
package com.project.Tadafur_api.application.service.analytics;

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

    private static final String DEFAULT_LANG = "en";

    /**
     * Gets the strategic health for ALL strategies.
     */
    public List<StrategicHealthDto> getAllStrategicHealths(String dateRange, String lang) {
        log.info("Calculating strategic health for all strategies.");
        List<Strategy> allStrategies = strategyRepository.findAll();

        return allStrategies.stream()
                .map(strategy -> getStrategicHealth(strategy.getId(), dateRange, lang))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the health for a SINGLE strategy.
     */
    public StrategicHealthDto getStrategicHealth(Long strategyId, String dateRange, String lang) {
        log.info("Calculating strategic health for Strategy ID: {}", strategyId);
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", strategyId));

        // 1. Fetch the entire hierarchy of entities
        HierarchyContainer hierarchy = findAllEntitiesForStrategy(strategyId);

        if (hierarchy.getProjects().isEmpty()) {
            log.warn("No projects found for strategy ID: {}. Cannot calculate health.", strategyId);
            return StrategicHealthDto.builder()
                    .strategyId(strategyId)
                    .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode))
                    .healthStatus("No Data")
                    .build();
        }

        // 2. Perform the BI calculations
        BigDecimal overallProgress = calculateOverallProgress(hierarchy.getProjects());
        BigDecimal budgetVariance = calculateBudgetVariance(hierarchy.getProjects());
        Double scheduleVariance = calculateScheduleVariance(hierarchy.getProjects());

        // 3. Determine overall status using the new, more intelligent logic
        String healthStatus = determineHealthStatus(hierarchy, overallProgress, budgetVariance, scheduleVariance);

        return StrategicHealthDto.builder()
                .strategyId(strategyId)
                .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode))
                .overallProgress(overallProgress)
                .budgetVariance(budgetVariance)
                .scheduleVarianceDays(scheduleVariance)
                .activeRisks(0) // Placeholder
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

    /**
     * NEW, MORE INTELLIGENT HEALTH STATUS LOGIC
     * This method now inspects the status codes of all entities in the hierarchy.
     */
    private String determineHealthStatus(HierarchyContainer hierarchy, BigDecimal progress, BigDecimal budgetVariance, Double scheduleVariance) {
        // Rule 1: Check for critical planning or progress errors first.
        // If any entity has a status that is not 'OK', it's an immediate flag.
        boolean hasCriticalError =
                hierarchy.getPerspectives().stream().anyMatch(e -> !e.getPlanningStatusCode().equalsIgnoreCase("OK")) ||
                        hierarchy.getGoals().stream().anyMatch(e -> !e.getPlanningStatusCode().equalsIgnoreCase("OK") || !e.getProgressStatusCode().equalsIgnoreCase("OK")) ||
                        hierarchy.getPrograms().stream().anyMatch(e -> !e.getPlanningStatusCode().equalsIgnoreCase("OK") || !e.getProgressStatusCode().equalsIgnoreCase("OK")) ||
                        hierarchy.getInitiatives().stream().anyMatch(e -> !e.getPlanningStatusCode().equalsIgnoreCase("OK") || !e.getProgressStatusCode().equalsIgnoreCase("OK")) ||
                        hierarchy.getProjects().stream().anyMatch(e -> !e.getPlanningStatusCode().equalsIgnoreCase("OK") || !e.getProgressStatusCode().equalsIgnoreCase("OK"));

        if (hasCriticalError) {
            return "Off-Track"; // A single non-OK status indicates a fundamental problem.
        }

        // Rule 2: If no critical errors, use the metric-based rules as a secondary check.
        if (progress.compareTo(new BigDecimal("75")) >= 0 && budgetVariance.compareTo(BigDecimal.ZERO) >= 0 && scheduleVariance <= 0) {
            return "On-Track";
        }
        if (progress.compareTo(new BigDecimal("50")) < 0 || budgetVariance.compareTo(BigDecimal.ZERO) < 0 || scheduleVariance > 15) {
            return "At-Risk";
        }

        // Default to On-Track if no other rules are met
        return "On-Track";
    }

    // Helper class to hold all entities in the hierarchy
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

    // --- Calculation methods remain the same ---

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
        long totalDelayDays = 0;
        long delayedProjectsCount = projects.stream()
                .filter(p -> {
                    boolean isInProgress = p.getProgressStatusCode() != null && !p.getProgressStatusCode().equalsIgnoreCase("COMPLETED");
                    return isInProgress && p.getEndDate() != null && today.isAfter(p.getEndDate());
                })
                .count();

        if (delayedProjectsCount == 0) return 0.0;

        totalDelayDays = projects.stream()
                .filter(p -> {
                    boolean isInProgress = p.getProgressStatusCode() != null && !p.getProgressStatusCode().equalsIgnoreCase("COMPLETED");
                    return isInProgress && p.getEndDate() != null && today.isAfter(p.getEndDate());
                })
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getEndDate(), today))
                .sum();

        return (double) totalDelayDays / delayedProjectsCount;
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }
}
