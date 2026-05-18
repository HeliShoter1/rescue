package com.rescue.rescue.service.Group;

import java.util.List;

import com.rescue.rescue.dto.UserDto;

public interface IGroupService {
    List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit);

    UserDto addUserToRescueTeam(Long rescueTeamId);

    UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId);
    
    
}
