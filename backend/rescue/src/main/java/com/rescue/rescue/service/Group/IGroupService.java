package com.rescue.rescue.service.Group;

import java.util.List;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.MemberStatus;

public interface IGroupService {
    List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit);

    UserDto AcceptUserToRescueTeam(Long rescueTeamId, Long userId, MemberStatus status);

    UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId);
    
    UserDto addUserToRescueTeamByPost(Long postId);
}
