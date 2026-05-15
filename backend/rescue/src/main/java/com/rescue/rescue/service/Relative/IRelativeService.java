package com.rescue.rescue.service.Relative;

import java.util.List;

import com.rescue.rescue.dto.RelativeDto;
import com.rescue.rescue.model.Relative;
import com.rescue.rescue.model.User;
import com.rescue.rescue.request.CreateRelative;

public interface IRelativeService {
    List<RelativeDto> getRelativesByUserId(Long cursor, Integer limit);
    RelativeDto createRelative(CreateRelative relative);
    void deleteRelative(Long id);
}
