package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.service.strategy.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/strategies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Strategy Management", description = "Endpoints for managing strategies with multi-language support.")
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a Strategy by ID (Multi-Language)",
            description = "Retrieves a single strategy, with name and description translated to the specified language.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Strategy found."),
            @ApiResponse(responseCode = "404", description = "Strategy not found.")
    })
    public ResponseEntity<StrategyResponseDto> getStrategyById(
            @Parameter(description = "Unique identifier of the strategy.", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Language code for translation (e.g., 'en', 'ar'). Defaults to 'en'.", example = "ar")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for strategy ID: {} with language: {}", id, lang);
        return ResponseEntity.ok(strategyService.getById(id, lang));
    }

    @GetMapping
    @Operation(summary = "Get All Strategies (Multi-Language)",
            description = "Retrieves a list of all strategies, with names and descriptions translated to the specified language.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of strategies.")
    public ResponseEntity<List<StrategyResponseDto>> getAllStrategies(
            @Parameter(description = "Language code for translation (e.g., 'en', 'ar'). Defaults to 'en'.", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        log.info("Received GET request for all strategies with language: {}", lang);
        return ResponseEntity.ok(strategyService.getAll(lang));
    }
}