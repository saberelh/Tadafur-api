// File: src/main/java/com/project/Tadafur_api/domain/organization/repository/AuthorityRepository.java
package com.project.Tadafur_api.domain.organization.repository;

import com.project.Tadafur_api.domain.organization.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}