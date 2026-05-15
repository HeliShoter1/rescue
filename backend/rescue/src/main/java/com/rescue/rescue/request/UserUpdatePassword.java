package com.rescue.rescue.request;

import jakarta.validation.constraints.NotBlank;

public class UserUpdatePassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
