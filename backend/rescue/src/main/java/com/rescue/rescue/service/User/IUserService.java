package com.rescue.rescue.service.User;

import java.util.List;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.model.User;
import com.rescue.rescue.request.CreateUserRequest;
import com.rescue.rescue.request.UserUpdatePassword;
import com.rescue.rescue.request.UserUpdateRole;
import com.rescue.rescue.request.UserUpdateStatus;

public interface IUserService {
    List<UserDto> getAllUsers(UserStatus status, UserRole role, Long cursor, Integer limit);
    UserDto getUserById(Long id);
    UserDto getUserByPhoneNumber(String phoneNumber);
    UserDto createUser(CreateUserRequest userRequest);
    UserDto updateUserStatus(UserUpdateStatus userUpdateStatus);
    UserDto updateUserPassword(UserUpdatePassword userUpdatePassword);
    UserDto updateUserRole(Long userId, UserUpdateRole userUpdateRole);
    UserDto convertDto(User user);
}
