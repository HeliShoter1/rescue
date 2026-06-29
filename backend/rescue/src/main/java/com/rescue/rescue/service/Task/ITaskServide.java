package com.rescue.rescue.service.Task;

import java.util.List;

import com.rescue.rescue.dto.TaskDto;
import com.rescue.rescue.enums.TaskStatus;
import com.rescue.rescue.model.Task;
import com.rescue.rescue.request.CreateTask;

public interface ITaskServide {
    
    TaskDto createTask(CreateTask task);
    TaskDto getTaskById(Long taskId);
    TaskDto updateTaskStatus(Long taskId, TaskStatus status);
    TaskDto registerTask(Long taskId);
    List<TaskDto> getTaskByUserId();
    List<TaskDto> getTaskByRescueTeamId(Long rescueTeamId);
}
