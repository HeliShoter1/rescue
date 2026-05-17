package com.rescue.rescue.dto;

import com.rescue.rescue.model.RescueTeam;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Builder
public class RescueTeamDto {
    private Long id;
    private PostDto post;
    private String status;

     public static RescueTeamDto fromEntity(RescueTeam rescueTeam) {
        return RescueTeamDto.builder()
                .id(rescueTeam.getId())
                .post(PostDto.fromEntity(rescueTeam.getPost()))
                .status(rescueTeam.getStatus().name())
                .build();
    }
}
