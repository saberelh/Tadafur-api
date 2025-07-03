package com.project.Tadafur_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategy-data")
public class WorkingStrategyController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/all")
    public List<Map<String, Object>> getAllStrategies() {
        List<Map<String, Object>> strategies = new ArrayList<>();

        try {
            Connection connection = dataSource.getConnection();
            String query = "SELECT id, primary_name, secondary_name, owner_id FROM \"2172_OM\".strategy  LIMIT 10";

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> strategy = new HashMap<>();
                strategy.put("id", rs.getLong("id"));
                strategy.put("primaryName", rs.getString("primary_name"));
                strategy.put("secondaryName", rs.getString("secondary_name"));
                strategy.put("ownerId", rs.getLong("owner_id"));
                strategies.add(strategy);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            strategies.add(error);
        }

        return strategies;
    }

    @GetMapping("/count")
    public Map<String, Object> getStrategyCount() {
        Map<String, Object> result = new HashMap<>();

        try {
            Connection connection = dataSource.getConnection();
            String query = "SELECT COUNT(*) as total FROM \"2172_OM\".strategy ";

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result.put("totalStrategies", rs.getInt("total"));
                result.put("status", "success");
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("status", "error");
        }

        return result;
    }
}