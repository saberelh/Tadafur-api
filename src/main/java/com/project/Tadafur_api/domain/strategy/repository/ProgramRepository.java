// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/ProgramRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    // Finds all programs linked to a specific goal ID
    List<Program> findByParentId(Long parentId);
}