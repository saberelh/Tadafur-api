// File: domain/common/entity/BaseEntity.java
package com.project.Tadafur_api.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity class with common audit fields
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "status_code", nullable = false)
    private String statusCode = "ACTIVE";

    /**
     * Check if entity is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.statusCode);
    }

    /**
     * Check if entity is deleted
     */
    public boolean isDeleted() {
        return "DELETED".equals(this.statusCode);
    }

    /**
     * Soft delete the entity
     */
    public void delete() {
        this.statusCode = "DELETED";
    }

    /**
     * Archive the entity
     */
    public void archive() {
        this.statusCode = "ARCHIVED";
    }

    /**
     * Activate the entity
     */
    public void activate() {
        this.statusCode = "ACTIVE";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (statusCode == null) {
            statusCode = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }
}