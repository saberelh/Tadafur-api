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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final InitiativeMapper initiativeMapper;

    /**
     * NEW METHOD: Gets initiatives based on an optional owner ID.
     */
    public List<InitiativeResponseDto> getInitiatives(Optional<Long> ownerId, String lang) {
        List<Initiative> initiatives;
        if (ownerId.isPresent()) {
            log.info("Fetching initiatives for owner ID: {}", ownerId.get());
            initiatives = initiativeRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Fetching all initiatives.");
            initiatives = initiativeRepository.findAll();
        }
        return initiativeMapper.toResponseDtoList(initiatives, lang);
    }

    /**
     * UNCHANGED METHOD: Gets a single initiative by its ID.
     */
    public InitiativeResponseDto getById(Long id, String lang) {
        log.info("Fetching initiative with ID: {} for language: {}", id, lang);
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative", "id", id));
        return initiativeMapper.toResponseDto(initiative, lang);
    }

    /**
     * UNCHANGED METHOD: Gets all initiatives belonging to a specific program.
     */
    public List<InitiativeResponseDto> getByProgramId(Long programId, String lang) {
        log.info("Fetching initiatives for program ID: {} and language: {}", programId, lang);
        List<Initiative> initiatives = initiativeRepository.findByParentId(programId);
        return initiativeMapper.toResponseDtoList(initiatives, lang);
    }
}