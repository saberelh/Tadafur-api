// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/StrategyRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Strategy Repository - Data access operations for Strategy entities.
 * UPDATED with native queries to search within JSONB columns.
 */
@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    /**
     * This query now uses a native PostgreSQL function `jsonb_each_text`
     * to search for a given text within all values of the name and description
     * translation maps. It performs a case-insensitive search (ILIKE).
     *
     * Note: We have removed the other search/filter methods for now
     * to ensure the application starts. They would also need to be
     * updated to use native queries if they search translatable fields.
     */
    @Query(
            value = "SELECT * FROM strategy s WHERE " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.name_translations) WHERE value ILIKE %:query%) OR " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.description_translations) WHERE value ILIKE %:query%) OR " +
                    "s.vision ILIKE %:query%",
            nativeQuery = true
    )
    Page<Strategy> searchStrategies(@Param("query") String query, Pageable pageable);

    @Query(
            value = "SELECT * FROM strategy s WHERE " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.name_translations) WHERE value ILIKE %:query%) OR " +
                    "EXISTS (SELECT 1 FROM jsonb_each_text(s.description_translations) WHERE value ILIKE %:query%) OR " +
                    "s.vision ILIKE %:query%",
            nativeQuery = true
    )
    List<Strategy> searchStrategiesList(@Param("query") String query);

}