// File: infrastructure/web/config/SwaggerConfig.java
package com.project.Tadafur_api.infrastructure.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI tadafurOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tadafur Strategic Planning API")
                        .description("REST API for Strategic Planning Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tadafur Team")
                                .email("support@tadafur.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}