package com.rescue.rescue.service.Group;

import java.util.List;

import com.rescue.rescue.dto.GroupDto;
import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.model.Group;

public interface IGroupService {
    List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit);

    UserDto AcceptUserToRescueTeam(Long rescueTeamId, Long userId);

    UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId);

    List<GroupDto> getMemberByRescueTeamIdAndStatus(Long rescueTeamId, MemberStatus status, Long cursor, Integer limit);

    List<GroupDto> getMemberByPostIdAndStatus(Long postId, MemberStatus status, Long cursor, Integer limit);
    
    UserDto addUserToRescueTeamByPost(Long postId);
}
