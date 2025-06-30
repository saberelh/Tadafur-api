package com.project.Tadafur_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

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
}