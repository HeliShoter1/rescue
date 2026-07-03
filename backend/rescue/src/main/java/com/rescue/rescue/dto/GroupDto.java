package com.rescue.rescue.dto;

import com.rescue.rescue.enums.MemberStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {
    private Long id;
    private Long userId;
    private Long rescueTeamId;
    private MemberStatus status;

    public static GroupDto fromEntity(com.rescue.rescue.model.Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .userId(group.getUser().getId())
                .rescueTeamId(group.getRescueTeam().getId())
                .status(group.getStatus())
                .build();
    }
}
