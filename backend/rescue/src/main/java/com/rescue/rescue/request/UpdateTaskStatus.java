package com.rescue.rescue.request;

import com.rescue.rescue.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTaskStatus {
    private TaskStatus taskStatus;
}
