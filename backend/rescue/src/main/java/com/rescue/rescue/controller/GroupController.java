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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.service.Group.IGroupService;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.Delegate;

@RestController
@RequestMapping("${api.prefix}/groups")
@AllArgsConstructor
public class GroupController {
    private final IGroupService groupService;
    

    @GetMapping("/rescue-teams/{rescueTeamId}/users")
    public ResponseEntity<ApiResponse> getUsersByRescueTeamId(
                                        @PathVariable String rescueTeamId,
                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
                                    ) {
        // Implementation for fetching users by rescue team ID
        return ResponseEntity.ok(new ApiResponse("Users fetched successfully", groupService.getUsersByRescueTeamId(Long.parseLong(rescueTeamId), cursor, limit)));
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/rescue-teams/{rescueTeamId}/users/{userId}/add-user")
    public ResponseEntity<ApiResponse> AcceptUserToRescueTeam(
                                        @PathVariable String rescueTeamId, @PathVariable String userId) {
        return ResponseEntity.ok(new ApiResponse("User added to rescue team successfully", groupService.AcceptUserToRescueTeam(Long.parseLong(rescueTeamId), Long.parseLong(userId))));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/rescue-teams/rescueteams/{rescueTeamId}/registered")
    public ResponseEntity<ApiResponse> getRegisteredMembersByRescueTeamId(
                                        @PathVariable String rescueTeamId,
                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(new ApiResponse("Registered members fetched successfully", groupService.getMemberByRescueTeamIdAndStatus(Long.parseLong(rescueTeamId), MemberStatus.PENDING, cursor, limit)));
    }

    
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/rescue-teams/posts/{postId}/registered")
    public ResponseEntity<ApiResponse> getRegisteredMembersByPostId(
                                        @PathVariable String postId,
                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(new ApiResponse("Registered members fetched successfully", groupService.getMemberByRescueTeamIdAndStatus(Long.parseLong(postId), MemberStatus.PENDING, cursor, limit)));
    }

    @GetMapping("/rescue-teams/rescueteams/{rescueTeamId}/members/registered")
    public ResponseEntity<ApiResponse> getMembersByRescueTeamId(
                                        @PathVariable String rescueTeamId,
                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10")    Integer limit) {
        return ResponseEntity.ok(new ApiResponse("Registered members fetched successfully", groupService.getMemberByRescueTeamIdAndStatus(Long.parseLong(rescueTeamId), MemberStatus.ACCEPTED, cursor, limit)));
    }

    @GetMapping("/rescue-teams/posts/{postId}/members/registered")
    public ResponseEntity<ApiResponse> getMembersByPostId(
                                        @PathVariable String postId,
                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10")    Integer limit) {
        return ResponseEntity.ok(new ApiResponse("Registered members fetched successfully", groupService.getMemberByPostIdAndStatus(Long.parseLong(postId), MemberStatus.ACCEPTED, cursor, limit)));
    }

    @PostMapping("/post/{postId}/add-user")
    public ResponseEntity<ApiResponse> RegisterUserToRescueTeamByPost(
                                        @PathVariable String postId) {
        return ResponseEntity.ok(new ApiResponse("User registered to rescue team successfully", groupService.addUserToRescueTeamByPost(Long.parseLong(postId))));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/rescue-teams/{rescueTeamId}/users/{userId}")
    public ResponseEntity<ApiResponse> removeUserFromRescueTeam(
                                        @PathVariable String rescueTeamId,
                                        @PathVariable String userId) {
        return ResponseEntity.ok(new ApiResponse("User removed from rescue team successfully", groupService.removeUserFromRescueTeam(Long.parseLong(userId), Long.parseLong(rescueTeamId))));
    }

}
