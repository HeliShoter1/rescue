// thư viện: spring-context, spring-tx, lombok, spring-data-jpa
package com.rescue.rescue.service.Group;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.GroupRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.sercurity.user.RescueUserDetail;
import com.rescue.rescue.service.Notification.INotificationService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final UserReponsitory userRepository;
    private final INotificationService notificationService;

    @Override
    public List<UserDto> getUsersByRescueTeamId(Long rescueTeamId, Long cursor, Integer limit) {
        return groupRepository.findUsersByRescueTeamId(rescueTeamId, cursor, limit)
                .stream()
                .map(UserDto::fromEntity)
                .toList();
    }


    @Override
    public UserDto AcceptUserToRescueTeam(Long rescueTeamId, Long userId, MemberStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        RescueTeam rescueTeam = rescueTeamRepository.findById(rescueTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("RescueTeam not found"));

        Group group = Group.builder()
                .user(user)
                .rescueTeam(rescueTeam)
                .status(status)
                .build();

        groupRepository.save(group);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto addUserToRescueTeamByPost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        RescueTeam rescueTeam = rescueTeamRepository.findByPostId(postId)
                .orElseThrow(() -> new ResourceNotFoundException("RescueTeam not found"));

        Group group = Group.builder()
                .user(user)
                .rescueTeam(rescueTeam)
                .build();

        groupRepository.save(group);
        User manager = groupRepository.findManagerByRescueTeamId(rescueTeam.getId());
        notificationService.sendViaQueue(manager.getId(), userId, "Thêm thành viên", user.getName() + " đã tham gia đội cứu hộ");
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId) {
        Group group = groupRepository.findByUserIdAndRescueTeamId(userId, rescueTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        User manager = groupRepository.findManagerByRescueTeamId(group.getRescueTeam().getId());
        group.setStatus(MemberStatus.DELETED);
        groupRepository.save(group);
        notificationService.sendViaQueue(manager.getId(), userId, "Xóa thành viên", group.getUser().getName() + " đã rời khỏi đội cứu hộ");
        return UserDto.fromEntity(group.getUser());
    }
}