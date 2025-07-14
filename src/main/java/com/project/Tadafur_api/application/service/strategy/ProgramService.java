// File: src/main/java/com/project/Tadafur_api/application/service/strategy/ProgramService.java
package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.ProgramResponseDto;
import com.project.Tadafur_api.application.mapper.strategy.ProgramMapper;
import com.project.Tadafur_api.domain.strategy.entity.Program;
import com.project.Tadafur_api.domain.strategy.repository.ProgramRepository;
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
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    /**
     * NEW METHOD: Gets programs based on an optional owner ID.
     */
    public List<ProgramResponseDto> getPrograms(Optional<Long> ownerId, String lang) {
        List<Program> programs;
        if (ownerId.isPresent()) {
            log.info("Fetching programs for owner ID: {}", ownerId.get());
            programs = programRepository.findByOwnerId(ownerId.get());
        } else {
            log.info("Fetching all programs.");
            programs = programRepository.findAll();
        }
        return programMapper.toResponseDtoList(programs, lang);
    }

    /**
     * UNCHANGED METHOD: Gets a single program by its ID.
     */
    public ProgramResponseDto getById(Long id, String lang) {
        log.info("Fetching program with ID: {} for language: {}", id, lang);
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "id", id));
        return programMapper.toResponseDto(program, lang);
    }

    /**
     * UNCHANGED METHOD: Gets all programs belonging to a specific goal.
     */
    public List<ProgramResponseDto> getByGoalId(Long goalId, String lang) {
        log.info("Fetching programs for goal ID: {} and language: {}", goalId, lang);
        List<Program> programs = programRepository.findByParentId(goalId);
        return programMapper.toResponseDtoList(programs, lang);
    }
}