package com.project.Tadafur_api.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableCaching
public class ApplicationConfig {

    /**
     * OpenAPI 3 Configuration for Swagger Documentation
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tadafur Strategic Planning Dashboard API")
                        .description("READ-ONLY APIs for Strategic Planning Dashboard & Executive Decision Making")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Tadafur Development Team")
                                .email("dev@tadafur.com")
                                .url("https://tadafur.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    /**
     * CORS Configuration for Dashboard Frontend
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow dashboard frontend origins
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",     // React dashboard
                "http://localhost:4200",     // Angular dashboard
                "http://localhost:8081",     // Alternative port
                "https://dashboard.tadafur.com",  // Production dashboard
                "https://*.tadafur.com"      // All Tadafur subdomains
        ));

        // Allow only READ operations (no POST, PUT, DELETE)
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "OPTIONS"  // Only GET and OPTIONS for READ-only API
        ));

        // Allow headers needed for dashboard
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "X-Requested-With",
                "Accept", "Origin", "Cache-Control"
        ));

        // Allow credentials for authenticated requests
        configuration.setAllowCredentials(true);

        // Cache preflight requests for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}