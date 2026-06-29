package com.rescue.rescue.dto;

import com.rescue.rescue.model.Task;

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
public class TaskDto {
    private Long id;
    private String content;
    private String status;
    private RescueTeamDto rescueTeam;
    private UserDto user;

    public static TaskDto fromEntity(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .content(task.getContent())
                .status(task.getStatus().name())
                .rescueTeam(task.getRescueTeam() != null ? RescueTeamDto.fromEntity(task.getRescueTeam()) : null)
                .user(task.getUser() != null ? UserDto.fromEntity(task.getUser()) : null)
                .build();
    }

}
