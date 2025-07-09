// File: src/main/java/com/project/Tadafur_api/application/service/strategy/GoalService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.GoalResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.GoalMapper;
import com.project.Tadafur_api.domain.strategy.entity.Goal;
import com.project.Tadafur_api.domain.strategy.repository.GoalRepository;
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
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

    /**
     * Gets a single goal by its ID, translated to the specified language.
     */
    public GoalResponseDto getById(Long id, String lang) {
        log.info("Fetching goal with ID: {} for language: {}", id, lang);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
        return goalMapper.toResponseDto(goal, lang);
    }

    /**
     * Gets all goals belonging to a specific perspective, translated.
     */
    public List<GoalResponseDto> getByPerspectiveId(Long perspectiveId, String lang) {
        log.info("Fetching goals for perspective ID: {} and language: {}", perspectiveId, lang);
        List<Goal> goals = goalRepository.findByParentId(perspectiveId);
        return goalMapper.toResponseDtoList(goals, lang);
    }
}