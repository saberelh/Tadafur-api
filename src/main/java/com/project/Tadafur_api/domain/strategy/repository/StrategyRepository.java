// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/StrategyRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    // Finds all strategies owned by a specific authority.
    // Used when the ownerId filter is present.
    List<Strategy> findByOwnerId(Long ownerId);
}