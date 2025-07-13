// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/PerspectiveRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerspectiveRepository extends JpaRepository<Perspective, Long> {

    // THIS METHOD IS UNCHANGED: It finds all perspectives for a specific strategy.
    List<Perspective> findByParentId(Long parentId);

    /**
     * NEW METHOD: Finds all perspectives owned by a specific authority.
     * This will be used for the new ownerId filter.
     */
    List<Perspective> findByOwnerId(Long ownerId);
}