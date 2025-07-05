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
    // Remove auditor configuration since we don't have audit fields in database
}