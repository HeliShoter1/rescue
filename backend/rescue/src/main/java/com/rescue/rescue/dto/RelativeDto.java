package com.rescue.rescue.dto;

import com.rescue.rescue.model.Relative;
import com.rescue.rescue.model.User;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class RelativeDto {
    private Long id;
    private UserDto user;
    private UserDto relative;
    private String relationship;
    public static RelativeDto fromEntity(Relative relative) {
        return RelativeDto.builder()
                .id(relative.getId())
                .user(UserDto.fromEntity(relative.getUser()))
                .relative(UserDto.fromEntity(relative.getRelative()))
                .relationship(relative.getRelationship().name())
                .build();
    }
}
