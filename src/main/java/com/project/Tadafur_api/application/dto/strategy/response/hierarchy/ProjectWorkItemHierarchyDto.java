package com.project.Tadafur_api.application.dto.strategy.response.hierarchy;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
@Data @Builder @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectWorkItemHierarchyDto {
    private Long id;
    private String name;
}