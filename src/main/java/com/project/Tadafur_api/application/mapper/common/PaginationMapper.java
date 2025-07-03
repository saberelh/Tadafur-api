// File: application/mapper/common/PaginationMapper.java
package com.project.Tadafur_api.application.mapper.common;

import com.project.Tadafur_api.application.dto.common.PaginationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Common mapper for pagination information
 */
@Component
public class PaginationMapper {

    /**
     * Convert Spring Page to PaginationDto
     */
    public PaginationDto toPaginationDto(Page<?> page) {
        if (page == null) {
            return null;
        }

        Pageable pageable = page.getPageable();
        Sort sort = pageable.getSort();

        String sortBy = null;
        String sortDirection = null;

        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            sortBy = order.getProperty();
            sortDirection = order.getDirection().name();
        }

        return PaginationDto.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
}