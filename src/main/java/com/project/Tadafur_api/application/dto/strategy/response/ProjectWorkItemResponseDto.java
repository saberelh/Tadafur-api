// File: src/main/java/com/project/Tadafur_api/application/dto/strategy/response/ProjectWorkItemResponseDto.java
package com.project.Tadafur_api.application.dto.strategy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectWorkItemResponseDto {
    private Long id;
    private Long projectId;
    private Long parentId;
    private String name;
    private String description;
    private Integer priorityId;
    private Integer statusId;
    private Integer assigneeUserId;
    private BigDecimal estimatedTime;
    private Integer estimatedTimeUnit;
    private BigDecimal actualTime;
    private Integer actualTimeUnit;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedDueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualDueDate;
    private BigDecimal progressPercent;
    private Long workItemGroupId;
    private Integer level;
    private Integer itemSort;
    private String verificationResult;
    private BigDecimal progressByEffort;
    private BigDecimal progressByAverage;
    private BigDecimal manualProgressByEffort;
    private BigDecimal manualProgressByAverage;
    private Boolean isAddedFromCustom;
}
