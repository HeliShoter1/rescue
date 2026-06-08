package com.rescue.rescue.service.History;

import java.util.List;

import com.rescue.rescue.dto.HistoryDto;
import com.rescue.rescue.enums.HistoryStatus;

public interface IHistoryService {
    List<HistoryDto> GetAllByAdmin(Long cursor, Integer limit);
    List<HistoryDto> GetAllByUserId( Long cursor, Integer limit);
    // HistoryDto CreateHistory(Long id);
    HistoryDto GetById();
    HistoryDto UpdateStatus(Long id, HistoryStatus status);
}
