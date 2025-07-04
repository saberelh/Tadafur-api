// File: shared/config/DataInitializer.java
package com.project.Tadafur_api.shared.config;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Data initializer for development/testing
 * Only runs in dev profile
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(StrategyRepository strategyRepository) {
        return args -> {
            log.info("Initializing sample data...");

            // Check if data already exists
            if (strategyRepository.count() > 0) {
                log.info("Data already exists, skipping initialization");
                return;
            }

            // Create sample strategies
            List<Strategy> strategies = Arrays.asList(
                    createStrategy(
                            "Digital Transformation Strategy 2024-2026",
                            "استراتيجية التحول الرقمي 2024-2026",
                            "Comprehensive digital transformation initiative to modernize our operations",
                            "مبادرة شاملة للتحول الرقمي لتحديث عملياتنا",
                            "To become a leading digital-first organization",
                            100L,
                            LocalDate.of(2024, 1, 1),
                            LocalDate.of(2026, 12, 31),
                            new BigDecimal("5000000"),
                            new BigDecimal("4800000"),
                            new BigDecimal("1200000")
                    ),
                    createStrategy(
                            "Customer Excellence Program",
                            "برنامج التميز في خدمة العملاء",
                            "Enhance customer satisfaction and loyalty through service excellence",
                            "تعزيز رضا العملاء وولائهم من خلال التميز في الخدمة",
                            "Deliver exceptional customer experiences at every touchpoint",
                            101L,
                            LocalDate.of(2024, 3, 1),
                            LocalDate.of(2025, 12, 31),
                            new BigDecimal("2000000"),
                            new BigDecimal("1950000"),
                            new BigDecimal("450000")
                    ),
                    createStrategy(
                            "Sustainability Initiative 2025",
                            "مبادرة الاستدامة 2025",
                            "Achieve carbon neutrality and sustainable operations by 2025",
                            "تحقيق الحياد الكربوني والعمليات المستدامة بحلول عام 2025",
                            "Lead the industry in environmental sustainability",
                            102L,
                            LocalDate.of(2023, 1, 1),
                            LocalDate.of(2025, 12, 31),
                            new BigDecimal("3500000"),
                            new BigDecimal("3400000"),
                            new BigDecimal("2100000")
                    ),
                    createStrategy(
                            "Innovation Hub Development",
                            "تطوير مركز الابتكار",
                            "Establish innovation centers to drive research and development",
                            "إنشاء مراكز الابتكار لدفع البحث والتطوير",
                            "Foster a culture of innovation and continuous improvement",
                            100L,
                            LocalDate.of(2024, 6, 1),
                            LocalDate.of(2026, 6, 30),
                            new BigDecimal("4000000"),
                            new BigDecimal("3900000"),
                            new BigDecimal("500000")
                    ),
                    createStrategy(
                            "Market Expansion Strategy",
                            "استراتيجية التوسع في السوق",
                            "Expand market presence in emerging regions",
                            "توسيع التواجد في الأسواق الناشئة",
                            "Become the market leader in key emerging markets",
                            103L,
                            LocalDate.of(2024, 4, 1),
                            LocalDate.of(2027, 3, 31),
                            new BigDecimal("6000000"),
                            new BigDecimal("5800000"),
                            new BigDecimal("800000")
                    )
            );

            strategyRepository.saveAll(strategies);
            log.info("Initialized {} sample strategies", strategies.size());
        };
    }

    private Strategy createStrategy(String primaryName, String secondaryName,
                                    String primaryDesc, String secondaryDesc,
                                    String vision, Long ownerId,
                                    LocalDate from, LocalDate to,
                                    BigDecimal plannedBudget, BigDecimal calcBudget,
                                    BigDecimal payments) {
        Strategy strategy = new Strategy();
        strategy.setPrimaryName(primaryName);
        strategy.setSecondaryName(secondaryName);
        strategy.setPrimaryDescription(primaryDesc);
        strategy.setSecondaryDescription(secondaryDesc);
        strategy.setVision(vision);
        strategy.setOwnerId(ownerId);
        strategy.setTimelineFrom(from);
        strategy.setTimelineTo(to);
        strategy.setPlannedTotalBudget(plannedBudget);
        strategy.setCalculatedTotalBudget(calcBudget);
        strategy.setCalculatedTotalPayments(payments);
        strategy.setStatusCode("ACTIVE");
        return strategy;
    }
}