// thư viện: spring-context, spring-tx, lombok, spring-data-jpa
package com.rescue.rescue.service.Group;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.GroupDto;
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
    public List<GroupDto> getMemberByRescueTeamIdAndStatus(Long rescueTeamId, MemberStatus status, Long cursor, Integer limit){
        return groupRepository.findByRescueTeamIdAndStatus(rescueTeamId, status, cursor, limit)
                .stream()
                .map(GroupDto::fromEntity)
                .toList();
    }

    @Override
    public List<GroupDto> getMemberByPostIdAndStatus(Long postId, MemberStatus status, Long cursor, Integer limit){
        return groupRepository.findByPostIdAndStatus(postId, status, cursor, limit)
                .stream()
                .map(GroupDto::fromEntity)
                .toList();
    }

    @Override
    public UserDto AcceptUserToRescueTeam(Long rescueTeamId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        RescueTeam rescueTeam = rescueTeamRepository.findById(rescueTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("RescueTeam not found"));
        groupRepository.updateMemberStatus(userId, rescueTeamId, MemberStatus.ACCEPTED);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto addUserToRescueTeamByPost(Long postId) {
        Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        RescueTeam rescueTeam = rescueTeamRepository.findByPostId(postId)
                .orElseThrow(() -> new ResourceNotFoundException("RescueTeam not found"));

        Group group = Group.builder()
                .user(user)
                .rescueTeam(rescueTeam)
                .status(MemberStatus.PENDING)
                .build();

        groupRepository.save(group);
        User manager = groupRepository.findManagerByRescueTeamId(rescueTeam.getId());
        notificationService.sendViaQueue(manager.getId(), userId, "Có thành viên đăng ký", user.getName() + " đã đăng ký tham gia đội cứu hộ");
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto removeUserFromRescueTeam(Long userId, Long rescueTeamId) {
        Group group = groupRepository.findByUserIdAndRescueTeamId(userId, rescueTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        User manager = groupRepository.findManagerByRescueTeamId(group.getRescueTeam().getId());
        groupRepository.updateMemberStatus(userId, rescueTeamId, MemberStatus.DELETED);
        notificationService.sendViaQueue(manager.getId(), userId, "Xóa thành viên", group.getUser().getName() + " đã rời khỏi đội cứu hộ");
        return UserDto.fromEntity(group.getUser());
    }
}