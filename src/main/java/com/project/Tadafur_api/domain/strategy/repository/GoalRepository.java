// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/GoalRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // Finds all goals linked to a specific perspective ID
    List<Goal> findByParentId(Long parentId);
}