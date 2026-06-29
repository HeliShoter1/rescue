package com.rescue.rescue.request;

import com.rescue.rescue.enums.TaskStatus;
import com.rescue.rescue.model.RescueTeam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTask {
    private String content;
    private TaskStatus status;
    private Long rescueTeamId;
}
