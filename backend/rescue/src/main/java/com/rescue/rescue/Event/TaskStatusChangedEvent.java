package com.rescue.rescue.Event;

import lombok.Getter;

@Getter
public class TaskStatusChangedEvent {
    private final Long taskId;

    public TaskStatusChangedEvent(Long taskId) {
        this.taskId = taskId;
    }
}
