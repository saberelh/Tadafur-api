// File: src/main/java/com/project/Tadafur_api/application/service/strategy/PerspectiveService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.PerspectiveResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.PerspectiveMapper;
import com.project.Tadafur_api.domain.strategy.entity.Perspective;
import com.project.Tadafur_api.domain.strategy.repository.PerspectiveRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerspectiveService {

    private final PerspectiveRepository perspectiveRepository;
    private final PerspectiveMapper perspectiveMapper;

    /**
     * Gets a single perspective by its ID, translated to the specified language.
     */
    public PerspectiveResponseDto getById(Long id, String lang) {
        log.info("Fetching perspective with ID: {} for language: {}", id, lang);
        Perspective perspective = perspectiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perspective", "id", id));
        return perspectiveMapper.toResponseDto(perspective, lang);
    }

    /**
     * Gets all perspectives belonging to a specific strategy, translated.
     */
    public List<PerspectiveResponseDto> getByStrategyId(Long strategyId, String lang) {
        log.info("Fetching perspectives for strategy ID: {} and language: {}", strategyId, lang);
        List<Perspective> perspectives = perspectiveRepository.findByParentId(strategyId);
        return perspectiveMapper.toResponseDtoList(perspectives, lang);
    }
}