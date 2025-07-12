// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/hierarchy/ProjectHierarchyDto.java
package com.project.Tadafur_api.application.dto.strategy.response.hierarchy;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data @Builder @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectHierarchyDto {
    private Long id;
    private String name;
    private List<ProjectWorkItemHierarchyDto> workItems;
}