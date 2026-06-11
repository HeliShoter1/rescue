package com.rescue.rescue.request;

import com.rescue.rescue.enums.RescueTeamStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UpdateRescueTeamRequest {
    private Long id;
    // private Long postId;
    private RescueTeamStatus status;
}
