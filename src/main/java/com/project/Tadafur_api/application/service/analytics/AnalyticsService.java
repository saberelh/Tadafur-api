// File: src/main/java/com/project/Tadafur_api/application/service/analytics/AnalyticsService.java
package com.project.Tadafur_api.application.service.analytics;

import com.project.Tadafur_api.application.dto.analytics.CumulativeSpendPoint;
import com.project.Tadafur_api.application.dto.analytics.ProjectSpendingTrendDto;
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
import java.util.*;
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
     * REFACTORED METHOD: Gets the strategic health for strategies,
     * with an optional filter for a specific owner.
     */
    public List<StrategicHealthDto> getStrategicHealths(Optional<Long> ownerId, String dateRange, String lang) {
        List<Strategy> strategiesToProcess;

        if (ownerId.isPresent()) {
            log.info("Calculating strategic health for owner ID: {}", ownerId.get());
            strategiesToProcess = strategyRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Calculating strategic health for all strategies.");
            strategiesToProcess = strategyRepository.findAll();
        }

        return strategiesToProcess.stream()
                .map(strategy -> calculateSingleStrategyHealth(strategy, dateRange, lang))
                .collect(Collectors.toList());
    }

    /**
     * This is the original getStrategicHealth method, renamed to be a more clear helper method.
     * It calculates the health for a SINGLE strategy.
     */
    public StrategicHealthDto calculateSingleStrategyHealth(Strategy strategy, String dateRange, String lang) {
        log.info("Calculating health for Strategy ID: {}", strategy.getId());
        String languageCode = Optional.ofNullable(lang).orElse(DEFAULT_LANG);

        List<Project> allProjects = findAllProjectsForStrategy(strategy.getId());

        if (allProjects.isEmpty()) {
            log.warn("No projects found for strategy ID: {}. Cannot calculate health.", strategy.getId());
            return StrategicHealthDto.builder()
                    .strategyId(strategy.getId())
                    .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode))
                    .healthStatus("No Data")
                    .build();
        }

        BigDecimal overallProgress = calculateOverallProgress(allProjects);
        BigDecimal budgetVariance = calculateBudgetVariance(allProjects);
        Double scheduleVariance = calculateScheduleVariance(allProjects);
        String healthStatus = determineHealthStatus(overallProgress, budgetVariance, scheduleVariance);

        return StrategicHealthDto.builder()
                .strategyId(strategy.getId())
                .strategyName(getTranslatedValue(strategy.getNameTranslations(), languageCode))
                .overallProgress(overallProgress)
                .budgetVariance(budgetVariance)
                .scheduleVarianceDays(scheduleVariance)
                .activeRisks(0) // Placeholder
                .healthStatus(healthStatus)
                .build();
    }

    // The private helper methods below are unchanged

    private List<Project> findAllProjectsForStrategy(Long strategyId) {
        return projectRepository.findAllDescendantProjectsByStrategyId(strategyId);
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

    private String determineHealthStatus(BigDecimal progress, BigDecimal budgetVariance, Double scheduleVariance) {
        if (progress.compareTo(new BigDecimal("75")) >= 0 && budgetVariance.compareTo(BigDecimal.ZERO) >= 0 && scheduleVariance <= 0) {
            return "On-Track";
        }
        if (progress.compareTo(new BigDecimal("40")) < 0 || budgetVariance.compareTo(BigDecimal.ZERO) < 0 || scheduleVariance > 30) {
            return "Off-Track";
        }
        return "At-Risk";
    }

    private String getTranslatedValue(Map<String, String> translations, String lang) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get(DEFAULT_LANG)))
                .orElse(null);
    }

    public List<ProjectSpendingTrendDto> getSpendingVsPlanTrend(Optional<Long> ownerId, Optional<Long> projectId, String lang) {
        log.info("Fetching spending trend for ownerId: {}, projectId: {}",
                ownerId.map(String::valueOf).orElse("ALL"),
                projectId.map(String::valueOf).orElse("ALL"));

        // 1. Determine which projects to analyze
        List<Project> projectsToAnalyze;
        if (projectId.isPresent()) {
            projectsToAnalyze = Collections.singletonList(projectRepository.findById(projectId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId.get())));
        } else if (ownerId.isPresent()) {
            projectsToAnalyze = projectRepository.findByOwnerId(ownerId.get());
        } else {
            projectsToAnalyze = projectRepository.findAll();
        }

        if (projectsToAnalyze.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Fetch all cumulative spend data in a single, efficient query
        List<Long> projectIds = projectsToAnalyze.stream().map(Project::getId).collect(Collectors.toList());
        List<CumulativeSpendPoint> allSpendPoints = projectRepository.findCumulativeSpendForProjects(projectIds);

        // Group spend points by projectId for easy lookup
        Map<Long, List<CumulativeSpendPoint>> spendPointsByProject = allSpendPoints.stream()
                .collect(Collectors.groupingBy(CumulativeSpendPoint::getProjectId));

        // 3. Process each project to build its trend line
        List<ProjectSpendingTrendDto> finalResult = new ArrayList<>();
        for (Project project : projectsToAnalyze) {
            List<CumulativeSpendPoint> projectSpendPoints = spendPointsByProject.getOrDefault(project.getId(), Collections.emptyList());

            List<ProjectSpendingTrendDto.DataPoint> dataPoints = new ArrayList<>();
            LocalDate startDate = project.getStartDate();
            LocalDate endDate = project.getEndDate();

            if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
                long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                int spendPointIndex = 0;
                BigDecimal lastKnownSpend = BigDecimal.ZERO;

                for (long i = 0; i < totalDays; i++) {
                    LocalDate currentDate = startDate.plusDays(i);

                    while (spendPointIndex < projectSpendPoints.size() &&
                            !projectSpendPoints.get(spendPointIndex).getPaymentDate().isAfter(currentDate)) {
                        lastKnownSpend = projectSpendPoints.get(spendPointIndex).getCumulativeSpend();
                        spendPointIndex++;
                    }

                    BigDecimal plannedSpend = project.getPlannedTotalBudget()
                            .multiply(new BigDecimal(i + 1))
                            .divide(new BigDecimal(totalDays), 2, RoundingMode.HALF_UP);

                    dataPoints.add(ProjectSpendingTrendDto.DataPoint.builder()
                            .date(currentDate)
                            .cumulativeActualSpend(lastKnownSpend)
                            .cumulativePlannedSpend(plannedSpend)
                            .build());
                }
            }

            finalResult.add(ProjectSpendingTrendDto.builder()
                    .projectId(project.getId())
                    .projectName(getTranslatedValue(project.getNameTranslations(), lang))
                    .dataPoints(dataPoints)
                    .build());
        }

        return finalResult;
    }
}