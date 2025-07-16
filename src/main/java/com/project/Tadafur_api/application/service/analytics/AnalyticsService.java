package com.project.Tadafur_api.application.service.analytics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Tadafur_api.application.dto.analytics.*;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
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
        BigDecimal totalPayments = paymentDtos.stream().map(PaymentTransactionDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
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

    // --- REFACTORED: High-Performance Strategic Health Methods ---

    public List<StrategicHealthDto> getStrategicHealth(String dateRange, Long ownerId, String lang) {
        log.info("Calculating strategic health using single query with ownerId: {}", ownerId);
        List<StrategyHealthDataResult> allHealthData = strategyRepository.getStrategicHealthData(null, ownerId);

        Map<Long, List<StrategyHealthDataResult>> groupedByStrategy = allHealthData.stream()
                .collect(Collectors.groupingBy(StrategyHealthDataResult::getStrategyId));

        return groupedByStrategy.values().stream()
                .map(strategyData -> buildHealthDtoFromData(strategyData, lang))
                .collect(Collectors.toList());
    }

    public StrategicHealthDto getStrategicHealthForSingleStrategy(Long strategyId, String dateRange, String lang) {
        log.info("Calculating strategic health for single Strategy ID: {} using single query.", strategyId);
        List<StrategyHealthDataResult> strategyData = strategyRepository.getStrategicHealthData(strategyId, null);

        if (strategyData == null || strategyData.isEmpty()) {
            Strategy strategy = strategyRepository.findById(strategyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", strategyId));
            return StrategicHealthDto.builder()
                    .strategyId(strategyId)
                    .strategyName(getTranslatedValue(strategy.getNameTranslations(), lang, "Unknown Strategy"))
                    .healthStatus("No Data")
                    .build();
        }
        return buildHealthDtoFromData(strategyData, lang);
    }

    private StrategicHealthDto buildHealthDtoFromData(List<StrategyHealthDataResult> strategyData, String lang) {
        StrategyHealthDataResult firstRecord = strategyData.get(0);
        BigDecimal overallProgress = calculateOverallProgressFromData(strategyData);
        BigDecimal budgetVariance = calculateBudgetVarianceFromData(strategyData);
        Double scheduleVariance = calculateScheduleVarianceFromData(strategyData);
        boolean hasCriticalError = strategyData.stream()
                .anyMatch(p -> (p.getPlanningStatusCode() != null && !"OK".equalsIgnoreCase(p.getPlanningStatusCode())) ||
                        (p.getProgressStatusCode() != null && !"OK".equalsIgnoreCase(p.getProgressStatusCode())));
        String healthStatus = "On-Track";
        if (hasCriticalError) {
            healthStatus = "Off-Track";
        } else if (overallProgress.compareTo(new BigDecimal("50")) < 0 || budgetVariance.compareTo(BigDecimal.ZERO) < 0 || scheduleVariance > 15) {
            healthStatus = "At-Risk";
        }
        return StrategicHealthDto.builder()
                .strategyId(firstRecord.getStrategyId())
                .strategyName(getTranslatedValue(parseJsonMap(firstRecord.getNameTranslations()), lang, "Unknown Strategy"))
                .overallProgress(overallProgress)
                .budgetVariance(budgetVariance)
                .scheduleVarianceDays(scheduleVariance)
                .activeRisks(0)
                .healthStatus(healthStatus)
                .build();
    }

    // --- Helper methods for health calculations, operating on the DTO results ---

    private BigDecimal calculateOverallProgressFromData(List<StrategyHealthDataResult> data) {
        if (data.isEmpty()) return BigDecimal.ZERO;
        BigDecimal totalProgress = data.stream()
                .map(p -> p.getCalculatedProgressPercent() != null ? p.getCalculatedProgressPercent() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalProgress.divide(new BigDecimal(data.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateBudgetVarianceFromData(List<StrategyHealthDataResult> data) {
        BigDecimal totalPlanned = data.stream()
                .map(p -> p.getPlannedTotalBudget() != null ? p.getPlannedTotalBudget() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalActual = data.stream()
                .map(p -> p.getActualCost() != null ? p.getActualCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPlanned.subtract(totalActual);
    }

    private Double calculateScheduleVarianceFromData(List<StrategyHealthDataResult> data) {
        LocalDate today = LocalDate.now();
        List<StrategyHealthDataResult> delayedProjects = data.stream()
                .filter(p -> p.getProgressStatusCode() != null && !"COMPLETED".equalsIgnoreCase(p.getProgressStatusCode()))
                .filter(p -> p.getEndDate() != null && today.isAfter(p.getEndDate()))
                .toList();
        if (delayedProjects.isEmpty()) return 0.0;
        long totalDelayDays = delayedProjects.stream()
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getEndDate(), today))
                .sum();
        return (double) totalDelayDays / delayedProjects.size();
    }

    private Map<String, String> parseJsonMap(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyMap();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Error parsing JSONB string from database: {}", json, e);
            return Collections.emptyMap();
        }
    }

    private String getTranslatedValue(Map<String, String> translations, String lang, String fallback) {
        return Optional.ofNullable(translations)
                .map(map -> map.getOrDefault(lang, map.get("en")))
                .orElse(fallback);
    }
}