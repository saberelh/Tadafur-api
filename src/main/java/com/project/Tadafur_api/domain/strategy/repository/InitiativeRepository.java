// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/InitiativeRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    // Finds all initiatives linked to a specific program ID
    List<Initiative> findByParentId(Long parentId);
}