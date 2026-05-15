package com.rescue.rescue.request;

import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;

import com.rescue.rescue.enums.UserStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserUpdateStatus {
    @NotBlank
    private UserStatus status;
}
