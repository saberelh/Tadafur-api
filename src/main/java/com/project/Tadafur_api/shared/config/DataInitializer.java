// File: src/main/java/com/project/Tadafur_api/shared/config/DataInitializer.java
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
import java.util.List;
import java.util.Map;

/**
 * Data initializer for development/testing.
 * Only runs in 'dev' profile.
 * UPDATED to support multi-language fields.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(StrategyRepository strategyRepository) {
        return args -> {
            log.info("Initializing sample data for development...");

            if (strategyRepository.count() > 0) {
                log.info("Data already exists. Skipping initialization.");
                return;
            }

            Strategy strategy1 = Strategy.builder()
                    .nameTranslations(Map.of(
                            "en", "Digital Transformation Strategy 2024-2026",
                            "ar", "استراتيجية التحول الرقمي 2024-2026"
                    ))
                    .descriptionTranslations(Map.of(
                            "en", "A comprehensive digital transformation initiative to modernize our operations.",
                            "ar", "مبادرة تحول رقمي شاملة لتحديث عملياتنا."
                    ))
                    .vision("To become a leading digital-first organization")
                    .ownerId(100L)
                    .timelineFrom(LocalDate.of(2024, 1, 1))
                    .timelineTo(LocalDate.of(2026, 12, 31))
                    .plannedTotalBudget(new BigDecimal("5000000"))
                    .calculatedTotalBudget(new BigDecimal("4800000"))
                    .calculatedTotalPayments(new BigDecimal("1200000"))
                    .build();

            Strategy strategy2 = Strategy.builder()
                    .nameTranslations(Map.of(
                            "en", "Customer Excellence Program",
                            "ar", "برنامج التميز في خدمة العملاء"
                    ))
                    .descriptionTranslations(Map.of(
                            "en", "Enhance customer satisfaction and loyalty through service excellence.",
                            "ar", "تعزيز رضا العملاء وولائهم من خلال التميز في الخدمة."
                    ))
                    .vision("Deliver exceptional customer experiences at every touchpoint")
                    .ownerId(101L)
                    .timelineFrom(LocalDate.of(2024, 3, 1))
                    .timelineTo(LocalDate.of(2025, 12, 31))
                    .plannedTotalBudget(new BigDecimal("2000000"))
                    .calculatedTotalBudget(new BigDecimal("1950000"))
                    .calculatedTotalPayments(new BigDecimal("450000"))
                    .build();

            strategyRepository.saveAll(List.of(strategy1, strategy2));
            log.info("Initialized 2 sample strategies.");
        };
    }
}