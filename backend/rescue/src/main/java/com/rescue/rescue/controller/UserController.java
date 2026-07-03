package com.rescue.rescue.controller;

import java.util.List; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreatePlace;
import com.rescue.rescue.request.CreateUserRequest;
import com.rescue.rescue.request.UserUpdateRole;
import com.rescue.rescue.request.UserUpdateStatus;
import com.rescue.rescue.request.UserUpdateStatus;
import com.rescue.rescue.service.User.IUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{user_id}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("user_id") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    }

    @GetMapping("/allusers")
    public ResponseEntity<ApiResponse> getAllUsers(
                            @RequestParam (value = "status", required = false) UserStatus status,
                            @RequestParam (value = "role", required = false) UserRole role,
                            @RequestParam(value="search", required = false) String search,
                            @RequestParam(value="cursor",required = true, defaultValue = "0") Long cursor,
                            @RequestParam(value="limit",required = true,defaultValue = "10") Integer limit) {
        // Implementation for fetching all users
        List<UserDto> users = userService.getAllUsers(status, role, search, cursor, limit);
        return ResponseEntity.ok(new ApiResponse("success", users));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody CreateUserRequest userRequest) {
        UserDto userDto = userService.createUser(userRequest);
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/{user_id}/rescue-teams/{rescue_team_id}/assign-manager")
    public ResponseEntity<ApiResponse> assignManagerToRescueTeam(@PathVariable("user_id") Long userId, @PathVariable("rescue_team_id") Long rescueTeamId) {
        userService.assignManagerToRescueTeam(userId, rescueTeamId);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PutMapping("/update/status")
    public ResponseEntity<ApiResponse> updateUserStatus(@RequestBody UserUpdateStatus userUpdate) {
        UserDto userDto = userService.updateUserStatus(userUpdate);
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    } 

    @PutMapping("/update/place")
    public ResponseEntity<ApiResponse> updateUserPlace(@RequestBody CreatePlace placeId) {
        UserDto userDto = userService.updateUserPlace(placeId);
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}/role")
    public ResponseEntity<ApiResponse> updateUserRole(@PathVariable Long id, @RequestBody UserUpdateRole userUpdate) {
        UserDto userDto = userService.updateUserRole(id, userUpdate);
        return ResponseEntity.ok(new ApiResponse("success", userDto));
    } 
}
