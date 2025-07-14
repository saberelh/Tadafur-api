// STEP 1: CREATE THE 'AUTHORITY' ENTITY AND REPOSITORY
// To validate the ownerId, we first need to define the Authority entity.
// =================================================================================

// File: src/main/java/com/project/Tadafur_api/domain/organization/entity/Authority.java
package com.project.Tadafur_api.domain.organization.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "authority")
@Data
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "name_translations", columnDefinition = "jsonb")
    private Map<String, String> nameTranslations;

    @Column(name = "logobase64", columnDefinition = "TEXT")
    private String logoBase64;

    @Column(name = "country_code", length = 2)
    private String countryCode;
}
