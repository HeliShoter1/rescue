package com.rescue.rescue.dto;


import java.time.LocalDate;

import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.model.User;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;
    private LocalDate createAt;
    private LocalDate updateAt;
    private LocalDate passwordChangedAt;
    private PlaceDto place;
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .passwordChangedAt(user.getPasswordChangedAt())
                .place(user.getPlace() != null ? PlaceDto.fromEntity(user.getPlace()) : null)
                .build();
    }
}
