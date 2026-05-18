// thư viện: spring-context, spring-tx, lombok, spring-data-jpa
package com.rescue.rescue.service.Group;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.GroupRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.sercurity.user.RescueUserDetail;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final UserReponsitory userRepository;

    @Override
    public List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit) {
        return groupRepository.findUsersByRescueTeamId(rescueTeamId, cursor, limit)
                .stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    public UserDto addUserToRescueTeam(Long rescueTeamId) {
        // Cần inject UserRepository và RescueTeamRepository để lấy entity
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RescueTeam rescueTeam = rescueTeamRepository.findById(rescueTeamId)
                .orElseThrow(() -> new RuntimeException("RescueTeam not found"));

        Group group = Group.builder()
                .user(user)
                .rescueTeam(rescueTeam)
                .build();

        groupRepository.save(group);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId) {
        Group group = groupRepository.findByUserIdAndRescueTeamId(userId, rescueTeamId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        groupRepository.delete(group);
        return UserDto.fromEntity(group.getUser());
    }
}