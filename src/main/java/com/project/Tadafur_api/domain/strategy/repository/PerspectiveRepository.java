// File: src/main/java/com/project/Tadafur_api/domain/strategy/repository/PerspectiveRepository.java
package com.project.Tadafur_api.domain.strategy.repository;

import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerspectiveRepository extends JpaRepository<Perspective, Long> {

    // Spring Data JPA automatically creates the query from the method name:
    // "SELECT * FROM perspective WHERE parent_id = ?"
    List<Perspective> findByParentId(Long parentId);
}