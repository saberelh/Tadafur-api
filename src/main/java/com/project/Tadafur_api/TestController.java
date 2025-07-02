package com.project.Tadafur_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;  // ✅ ADDED THIS IMPORT
import java.sql.ResultSet;         // ✅ ADDED THIS IMPORT

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/hello")
    public String hello() {
        return "Hello! Tadafur API is running! 🚀";
    }

    @GetMapping("/db-connection")
    public String testDatabaseConnection() {
        try {
            Connection connection = dataSource.getConnection();
            String databaseName = connection.getCatalog();
            String url = connection.getMetaData().getURL();
            connection.close();

            return "✅ Database connected successfully!" +
                    "<br>Database: " + databaseName +
                    "<br>URL: " + url +
                    "<br>Connection successful!";
        } catch (Exception e) {
            return "❌ Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/db-tables")
    public String checkTables() {
        try {
            Connection connection = dataSource.getConnection();
            var metaData = connection.getMetaData();
            var tables = metaData.getTables(null, null, "strategy", null);

            StringBuilder result = new StringBuilder("Database Tables Check:<br>");

            if (tables.next()) {
                result.append("✅ 'strategy' table found!<br>");
            } else {
                result.append("❌ 'strategy' table not found<br>");
            }

            // Check perspective table
            tables = metaData.getTables(null, null, "perspective", null);
            if (tables.next()) {
                result.append("✅ 'perspective' table found!<br>");
            } else {
                result.append("❌ 'perspective' table not found<br>");
            }

            connection.close();
            return result.toString();

        } catch (Exception e) {
            return "❌ Error checking tables: " + e.getMessage();
        }
    }

    // ✅ NEW METHOD: Check strategy data
    @GetMapping("/strategy-data")
    public String checkStrategyData() {
        try {
            Connection connection = dataSource.getConnection();

            // Check if strategy table exists and has data
            String query = "SELECT COUNT(*) as total FROM \"2172_OM\".strategy";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int totalStrategies = 0;
            if (rs.next()) {
                totalStrategies = rs.getInt("total");
            }

            // Check a sample strategy
            String sampleQuery = "SELECT id, primary_name, secondary_name  FROM \"2172_OM\".strategy LIMIT 3";
            PreparedStatement sampleStmt = connection.prepareStatement(sampleQuery);
            ResultSet sampleRs = sampleStmt.executeQuery();

            StringBuilder result = new StringBuilder();
            result.append("✅ Strategy Table Check:<br>");
            result.append("Total Strategies: ").append(totalStrategies).append("<br><br>");

            if (totalStrategies > 0) {
                result.append("Sample Strategies:<br>");
                while (sampleRs.next()) {
                    result.append("ID: ").append(sampleRs.getLong("id"))
                            .append(", Arabic: ").append(sampleRs.getString("primary_name"))
                            .append(", English: ").append(sampleRs.getString("secondary_name"))
                            .append("<br>");
                }
            } else {
                result.append("❌ No strategy data found in database<br>");
                result.append("💡 You may need to add some test data first");
            }

            rs.close();
            stmt.close();
            sampleRs.close();
            sampleStmt.close();
            connection.close();
            return result.toString();

        } catch (Exception e) {
            return "❌ Error checking strategy data: " + e.getMessage();
        }
    }
}