package com.rescue.rescue.controller;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.service.Group.IGroupService;

import lombok.AllArgsConstructor;
import lombok.Delegate;

@RestController
@RequestMapping("${api.prefix}/groups")
@AllArgsConstructor
public class GroupController {
    private final IGroupService groupService;
    

    @GetMapping("/rescue-teams/{rescueTeamId}/users")
    public ResponseEntity<ApiResponse> getUsersByRescueTeamId(
                                        @PathVariable String rescueTeamId) {
        // Implementation for fetching users by rescue team ID
        return ResponseEntity.ok(new ApiResponse("Users fetched successfully", groupService.getUsersByRescueTeamId(Long.parseLong(rescueTeamId), null, null)));
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/rescue-teams/{rescueTeamId}/users/{userId}/add-user")
    public ResponseEntity<ApiResponse> AcceptUserToRescueTeam(
                                        @RequestBody String rescueTeamId, @RequestBody String userId, @RequestBody MemberStatus status) {
        return ResponseEntity.ok(new ApiResponse("User added to rescue team successfully", groupService.AcceptUserToRescueTeam(Long.parseLong(rescueTeamId), Long.parseLong(userId), status)));
    }

    @PostMapping("/post/{postId}/add-user")
    public ResponseEntity<ApiResponse> RegisterUserToRescueTeamByPost(
                                        @PathVariable String postId) {
        return ResponseEntity.ok(new ApiResponse("User added to rescue team successfully", groupService.addUserToRescueTeamByPost(Long.parseLong(postId))));
    }

    @DeleteMapping("/rescue-teams/{rescueTeamId}/users/{userId}")
    public ResponseEntity<ApiResponse> removeUserFromRescueTeam(
                                        @PathVariable String rescueTeamId,
                                        @PathVariable String userId) {
        return ResponseEntity.ok(new ApiResponse("User removed from rescue team successfully", groupService.removeUserFromRescueTeam(Long.parseLong(userId), Long.parseLong(rescueTeamId))));
    }

}
