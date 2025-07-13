// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/InitiativeRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    // This method is unchanged: It finds all initiatives for a specific program.
    List<Initiative> findByParentId(Long parentId);

    /**
     * NEW METHOD: Finds all initiatives owned by a specific authority.
     */
    List<Initiative> findByOwnerId(Long ownerId);
}