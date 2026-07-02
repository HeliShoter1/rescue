package com.rescue.rescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatsDTO {
    private long completedCount;
    private long inProgressCount;
    private long pendingCount;
    private Double avgCompletionMinutes; 
}
