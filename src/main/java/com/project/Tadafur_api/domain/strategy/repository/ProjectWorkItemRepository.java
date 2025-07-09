// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/ProjectWorkItemRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.ProjectWorkItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectWorkItemRepository extends JpaRepository<ProjectWorkItem, Long> {

    // Finds all work items linked to a specific project ID
    // We use projectId here because that's the field name in the entity
    List<ProjectWorkItem> findByProjectId(Long projectId);
}