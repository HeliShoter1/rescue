package com.rescue.rescue.request;

import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.model.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Valid
public class UserUpdateRole {
    @NotBlank
    @NotEmpty
    UserRole role;
}
