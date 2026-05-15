package com.rescue.rescue.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateStatus {
    @NotBlank
    private String status;
}
