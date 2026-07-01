package com.rescue.rescue.request;

import com.rescue.rescue.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRelativeStatus {
    private Long userId;
    private Long relativeId;
    private UserStatus status;
}
