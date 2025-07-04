// File: infrastructure/web/controller/DebugController.java
package com.project.Tadafur_api.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StrategyRepository strategyRepository;

    @GetMapping("/tables")
    public Map<String, Object> checkTables() {
        Map<String, Object> response = new HashMap<>();

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Check if strategy table exists
            String checkTableQuery = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = 'strategy'
                );
                """;

            Boolean tableExists = jdbcTemplate.queryForObject(checkTableQuery, Boolean.class);
            response.put("strategy_table_exists", tableExists);

            // Check current schema
            String currentSchema = jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
            response.put("current_schema", currentSchema);

            // List all tables
            List<String> tables = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = current_schema()",
                    String.class
            );
            response.put("tables_in_current_schema", tables);

            // Try to count strategies
            try {
                long count = strategyRepository.count();
                response.put("strategy_count", count);
            } catch (Exception e) {
                response.put("strategy_count_error", e.getMessage());
            }

        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("error_type", e.getClass().getSimpleName());
        }

        return response;
    }

    @GetMapping("/test-query")
    public Map<String, Object> testQuery() {
        Map<String, Object> response = new HashMap<>();

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Try different queries
            try {
                jdbcTemplate.execute("SELECT 1 FROM strategy LIMIT 1");
                response.put("query_strategy_table", "SUCCESS");
            } catch (Exception e) {
                response.put("query_strategy_table", "FAILED: " + e.getMessage());
            }

            // Check with schema
            try {
                jdbcTemplate.execute("SELECT 1 FROM \"2172_OM\".strategy LIMIT 1");
                response.put("query_with_schema", "SUCCESS");
            } catch (Exception e) {
                response.put("query_with_schema", "FAILED: " + e.getMessage());
            }

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}