// File: application/dto/common/PaginationDto.java
package com.project.Tadafur_api.application.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common Pagination DTO for paginated responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    private String sortBy;
    private String sortDirection;
}