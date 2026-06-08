package com.rescue.rescue.service.History;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.HistoryDto;
import com.rescue.rescue.enums.HistoryStatus;
import com.rescue.rescue.model.History;
import com.rescue.rescue.reponsitory.HistoryRepository;
import com.rescue.rescue.sercurity.user.RescueUserDetail;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class HistoryService implements IHistoryService {

    private final HistoryRepository historyRepository;
    

    @Override
    public List<HistoryDto> GetAllByAdmin(Long cursor, Integer limit) {
        // TODO Auto-generated method stub
        List<HistoryDto> historyDto = historyRepository.GetAllByAdmin(cursor, limit)
                .stream()
                .map(HistoryDto::fromEntity)
                .toList();
        return historyDto;
    }

    @Override
    public List<HistoryDto> GetAllByUserId(Long cursor, Integer limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        // TODO Auto-generated method stub
        List<HistoryDto> historyDto = historyRepository.GetAllByUserId(userId, cursor, limit)
                .stream()
                .map(HistoryDto::fromEntity)
                .toList();
        return historyDto;
    }

    @Override
    public HistoryDto GetById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        // TODO Auto-generated method stub
        HistoryDto historyDto = historyRepository.findById(userId)
                .map(HistoryDto::fromEntity)
                .orElse(null);
        return historyDto;
    }

    // @Override
    // public HistoryDto CreateHistory(Long id) {
    //     // TODO Auto-generated method stub
    //     History history = History.builder()
    //             .userId(id)
    //             .status(HistoryStatus.PENDING)
    //             .build();
    //     History savedHistory = historyRepository.save(history);
    //     return HistoryDto.fromEntity(savedHistory);
    // }

    @Override
    public HistoryDto UpdateStatus(Long id, HistoryStatus status) {
        // TODO Auto-generated method stub
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found with id: " + id));
        history.setStatus(status);
        History savedHistory = historyRepository.save(history);
        return HistoryDto.fromEntity(savedHistory);
    }
}