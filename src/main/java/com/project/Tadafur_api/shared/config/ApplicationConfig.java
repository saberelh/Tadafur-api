// File: shared/config/ApplicationConfig.java
package com.project.Tadafur_api.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Main application configuration
 */
@Configuration
public class ApplicationConfig {

    /**
     * Auditor provider for JPA Auditing
     * In a real application, this would return the current user from security context
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system"); // Default to "system" for now
    }
}