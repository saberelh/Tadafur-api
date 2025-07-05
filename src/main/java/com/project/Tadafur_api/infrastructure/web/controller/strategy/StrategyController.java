package com.project.Tadafur_api.infrastructure.web.controller.strategy;

import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.service.strategy.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/strategies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Strategy Management", description = "API endpoints for managing strategies")
public class StrategyController {

    private final StrategyService service;

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping
    @Operation(summary = "Get all strategies", description = "Retrieve all strategies with pagination support")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved strategies"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String,Object>> getAll(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("GET /strategies page={} size={} sort={} {}", page, size, sortBy, sortDirection);
        return ResponseEntity.ok(service.getAll(page, size, sortBy, sortDirection));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/{id}
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/{id}")
    @Operation(summary = "Get strategy by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<StrategyResponseDto> getById(@PathVariable Long id) {
        log.info("GET /strategies/{}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/search
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/search")
    @Operation(summary = "Search strategies")
    public ResponseEntity<Map<String,Object>> search(
            @Parameter(description = "Search query", required = true)
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("SEARCH /strategies q={} page={} size={}", query, page, size);
        return ResponseEntity.ok(service.search(query, page, size));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/by-owner/{ownerId}
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/by-owner/{ownerId}")
    @Operation(summary = "Get strategies by owner")
    public ResponseEntity<Map<String,Object>> byOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("OWNER /strategies owner={} page={} size={}", ownerId, page, size);
        return ResponseEntity.ok(service.getByOwner(ownerId, page, size));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/active
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/active")
    @Operation(summary = "Get active strategies")
    public ResponseEntity<Map<String,Object>> active(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("ACTIVE /strategies page={} size={}", page, size);
        return ResponseEntity.ok(service.getActive(page, size));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/by-timeline
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/by-timeline")
    @Operation(summary = "Get strategies by timeline range")
    public ResponseEntity<Map<String,Object>> byTimeline(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("TIMELINE /strategies from={} to={} page={} size={}", from, to, page, size);
        return ResponseEntity.ok(service.getByTimeline(from, to, page, size));
    }

    /*───────────────────────────────────────────────────────────────────────────
     * GET /api/v1/strategies/summary
     *──────────────────────────────────────────────────────────────────────────*/
    @GetMapping("/summary")
    @Operation(summary = "Get global strategy summary")
    public ResponseEntity<Map<String,Object>> summary() {
        log.info("SUMMARY /strategies");
        return ResponseEntity.ok(service.getSummary());
    }
}
