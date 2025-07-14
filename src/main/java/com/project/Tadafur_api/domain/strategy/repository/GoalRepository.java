// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/GoalRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // This method is unchanged: It finds all goals for a specific perspective.
    List<Goal> findByParentId(Long parentId);

    /**
     * NEW METHOD: Finds all goals owned by a specific authority.
     * This will be used for the new ownerId filter.
     */
    List<Goal> findByOwnerId(Long ownerId);
}