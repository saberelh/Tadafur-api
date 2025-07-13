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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

    /**
     * NEW METHOD: Gets goals based on an optional owner ID.
     */
    public List<GoalResponseDto> getGoals(Optional<Long> ownerId, String lang) {
        List<Goal> goals;
        if (ownerId.isPresent()) {
            log.info("Fetching goals for owner ID: {}", ownerId.get());
            goals = goalRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Fetching all goals.");
            goals = goalRepository.findAll();
        }
        return goalMapper.toResponseDtoList(goals, lang);
    }

    /**
     * UNCHANGED METHOD: Gets a single goal by its ID.
     */
    public GoalResponseDto getById(Long id, String lang) {
        log.info("Fetching goal with ID: {} for language: {}", id, lang);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
        return goalMapper.toResponseDto(goal, lang);
    }

    /**
     * UNCHANGED METHOD: Gets all goals belonging to a specific perspective.
     */
    public List<GoalResponseDto> getByPerspectiveId(Long perspectiveId, String lang) {
        log.info("Fetching goals for perspective ID: {} and language: {}", perspectiveId, lang);
        List<Goal> goals = goalRepository.findByParentId(perspectiveId);
        return goalMapper.toResponseDtoList(goals, lang);
    }
}