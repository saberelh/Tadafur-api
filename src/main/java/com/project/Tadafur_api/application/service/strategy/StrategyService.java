// File: src/main/java/com/project/Tadafur_api/application/service/strategy/StrategyService.java
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepository;
    private final StrategyMapper strategyMapper;

    /**
     * REFACTORED METHOD: Gets strategies based on an optional owner ID.
     * If ownerId is provided, it filters by that owner.
     * If ownerId is absent, it returns all strategies.
     */
    public List<StrategyResponseDto> getStrategies(Optional<Long> ownerId, String lang) {
        List<Strategy> strategies;
        if (ownerId.isPresent()) {
            log.info("Fetching strategies for owner ID: {}", ownerId.get());
            strategies = strategyRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Fetching all strategies.");
            strategies = strategyRepository.findAll();
        }
        return strategyMapper.toResponseDtoList(strategies, lang);
    }

    /**
     * Gets a single strategy by its ID. (This method is unchanged).
     */
    public StrategyResponseDto getById(Long id, String lang) {
        log.info("Fetching strategy with ID: {} for language: {}", id, lang);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", id));
        return strategyMapper.toResponseDto(strategy, lang);
    }
}