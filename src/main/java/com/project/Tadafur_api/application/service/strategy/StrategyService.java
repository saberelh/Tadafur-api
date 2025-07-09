package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.StrategyMapper;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer containing business logic for Strategy operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepository;
    private final StrategyMapper strategyMapper;

    /**
     * Gets a single strategy by ID, translated to the specified language.
     * @param id The ID of the strategy.
     * @param lang The language code (e.g., 'en', 'ar').
     * @return The translated Strategy DTO.
     */
    public StrategyResponseDto getById(Long id, String lang) {
        log.info("Fetching strategy with ID: {} for language: {}", id, lang);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", id));
        return strategyMapper.toResponseDto(strategy, lang);
    }

    /**
     * Gets all strategies, translated to the specified language.
     * @param lang The language code (e.g., 'en', 'ar').
     * @return A list of translated Strategy DTOs.
     */
    public List<StrategyResponseDto> getAll(String lang) {
        log.info("Fetching all strategies for language: {}", lang);
        List<Strategy> strategies = strategyRepository.findAll();
        return strategyMapper.toResponseDtoList(strategies, lang);
    }
}