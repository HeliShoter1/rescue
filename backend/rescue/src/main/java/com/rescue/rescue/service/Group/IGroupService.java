package com.rescue.rescue.service.Group;

import java.util.List;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.model.Group;

public interface IGroupService {
    List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit);

    UserDto AcceptUserToRescueTeam(Long rescueTeamId, Long userId, MemberStatus status);

    UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId);

    List<Group> getMemberByRescueTeamIdAndStatus(Long rescueTeamId, MemberStatus status, Long cursor, Integer limit);

    List<Group> getMemberByPostIdAndStatus(Long postId, MemberStatus status, Long cursor, Integer limit);
    
    UserDto addUserToRescueTeamByPost(Long postId);
}
