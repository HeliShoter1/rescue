package com.rescue.rescue.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UpdateRescueTeamRequest {
    private Long id;
    private Long postId;
    private String status;
}
