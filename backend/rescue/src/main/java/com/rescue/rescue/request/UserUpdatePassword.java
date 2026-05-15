package com.rescue.rescue.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserUpdatePassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
