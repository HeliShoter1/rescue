package com.rescue.rescue.dto;


import java.time.LocalDate;

import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.model.User;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDto {
    private String name;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;
    private LocalDate createAt;
    private LocalDate updateAt;
    private LocalDate passwordChangedAt;
    public static UserDto fromEntity(User user) {
    return UserDto.builder()
            .name(user.getName())
            .phoneNumber(user.getPhoneNumber())
            .role(user.getRole())
            .status(user.getStatus())
            .createAt(user.getCreateAt())
            .updateAt(user.getUpdateAt())
            .passwordChangedAt(user.getPasswordChangedAt())
            .build();
}
}
