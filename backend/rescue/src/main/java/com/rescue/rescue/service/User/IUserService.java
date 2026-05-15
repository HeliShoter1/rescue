package com.rescue.rescue.service.User;

import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.model.User;

public interface IUserService {
    UserDto getUserById(Long id);
    UserDto getUserByPhoneNumber(String phoneNumber);
    UserDto createUser(UserDto userDto);
    UserDto updateUserStatus(Long id, String status);
    UserDto updateUserPassword(Long id, String oldPassword, String newPassword);
    UserDto convertDto(User user);
}
