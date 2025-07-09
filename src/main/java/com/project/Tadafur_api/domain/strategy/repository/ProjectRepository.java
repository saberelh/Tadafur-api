// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/ProjectRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Finds all projects linked to a specific initiative ID
    List<Project> findByParentId(Long parentId);
}