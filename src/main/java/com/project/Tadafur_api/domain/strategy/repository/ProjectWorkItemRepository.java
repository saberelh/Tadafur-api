// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/ProjectWorkItemRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.ProjectWorkItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectWorkItemRepository extends JpaRepository<ProjectWorkItem, Long> {

    // This method is unchanged: It finds all work items for a specific project.
    List<ProjectWorkItem> findByProjectId(Long projectId);

    /**
     * NEW METHOD: Finds all work items assigned to a specific user.
     */
    List<ProjectWorkItem> findByAssigneeUserId(Integer assigneeUserId);
}
