package com.project.Tadafur_api.application.service.strategy;

import com.project.Tadafur_api.application.dto.common.PaginationDto;
import com.project.Tadafur_api.application.dto.strategy.response.StrategyResponseDto;
import com.project.Tadafur_api.application.mapper.common.PaginationMapper;
import com.project.Tadafur_api.application.mapper.strategy.StrategyMapper;
import com.project.Tadafur_api.domain.strategy.entity.Strategy;
import com.project.Tadafur_api.domain.strategy.repository.StrategyRepository;
import com.project.Tadafur_api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyRepository strategyRepo;
    private final StrategyMapper     mapper;
    private final PaginationMapper   pageMapper;

    /* ───────────── ALL ───────────── */
    public Map<String,Object> getAll(int page, int size, String sortBy, String dir) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(dir), sortBy));

        Page<Strategy> pg = strategyRepo.findAll(pageable);
        return wrap(pg, null);
    }

    /* ───────────── BY ID ───────────── */
    public StrategyResponseDto getById(Long id) {
        Strategy s = strategyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", id));
        return mapper.toResponseDto(s);
    }

    /* ───────────── SEARCH ───────────── */
    public Map<String,Object> search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Strategy> pg = strategyRepo.search(q, pageable);
        return wrap(pg, Map.of("query", q));
    }

    /* ───────────── BY OWNER ───────────── */
    public Map<String,Object> getByOwner(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Strategy> pg = strategyRepo.findByOwnerIdOrderByIdDesc(ownerId, pageable);

        Map<String,Object> extra = new HashMap<>();
        extra.put("ownerId", ownerId);
        extra.put("summary", strategyRepo.getSummaryByOwner(ownerId));
        return wrap(pg, extra);
    }

    /* ───────────── ACTIVE ───────────── */
    public Map<String,Object> getActive(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timelineFrom"));
        Page<Strategy> pg = strategyRepo.findCurrentlyActive(pageable);

        return wrap(pg, Map.of("currentDate", LocalDate.now()));
    }

    /* ───────────── BY TIMELINE ───────────── */
    public Map<String,Object> getByTimeline(LocalDate from, LocalDate to, int page, int size) {
        if (from.isAfter(to)) throw new IllegalArgumentException("'from' after 'to'");

        Pageable pageable = PageRequest.of(page, size, Sort.by("timelineFrom"));
        Page<Strategy> pg = strategyRepo.findByTimelineRange(from, to, pageable);

        return wrap(pg, Map.of("timelineFrom", from, "timelineTo", to));
    }

    /* ───────────── GLOBAL SUMMARY ───────────── */
    public Map<String,Object> getSummary() {
        return strategyRepo.getGlobalSummary();
    }

    /* ───────────── helper ───────────── */
    private Map<String,Object> wrap(Page<Strategy> pg, Map<String,Object> extra) {
        List<StrategyResponseDto> dtos = mapper.toResponseDtoList(pg.getContent());
        PaginationDto pagination       = pageMapper.toPaginationDto(pg);

        Map<String,Object> res = new HashMap<>();
        res.put("strategies", dtos);
        res.put("pagination", pagination);
        if (extra != null) res.putAll(extra);
        return res;
    }
}
