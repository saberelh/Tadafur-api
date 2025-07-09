// File: src/main/java/com/project/Tadafur_api/application/service/strategy/InitiativeService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.InitiativeResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.InitiativeMapper;
import com.project.Tadafur_api.domain.strategy.entity.Initiative;
import com.project.Tadafur_api.domain.strategy.repository.InitiativeRepository;
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
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final InitiativeMapper initiativeMapper;

    /**
     * Gets a single initiative by its ID, translated to the specified language.
     */
    public InitiativeResponseDto getById(Long id, String lang) {
        log.info("Fetching initiative with ID: {} for language: {}", id, lang);
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative", "id", id));
        return initiativeMapper.toResponseDto(initiative, lang);
    }

    /**
     * Gets all initiatives belonging to a specific program, translated.
     */
    public List<InitiativeResponseDto> getByProgramId(Long programId, String lang) {
        log.info("Fetching initiatives for program ID: {} and language: {}", programId, lang);
        List<Initiative> initiatives = initiativeRepository.findByParentId(programId);
        return initiativeMapper.toResponseDtoList(initiatives, lang);
    }
}