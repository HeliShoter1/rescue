package com.rescue.rescue.service.Task;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.rescue.rescue.Event.TaskStatusChangedEvent;
import com.rescue.rescue.dto.TaskDto;
import com.rescue.rescue.dto.TaskStatsDTO;
import com.rescue.rescue.enums.TaskStatus;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Task;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.reponsitory.TaskRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.request.CreateTask;
import com.rescue.rescue.sercurity.user.RescueUserDetail;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class TaskService  implements ITaskServide {
    
    private final TaskRepository taskRepository;

    private final RescueTeamRepository rescueTeamRepository;

    private final UserReponsitory userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public TaskDto getTaskById(Long taskId) {
        return TaskDto.fromEntity(taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId)));
    }

    @Override
    public void registerTask(Long taskId) {
        Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (task.getUser() != null) {
            throw new RuntimeException("Task is already registered by another user");
        }
        taskRepository.RegisterTask(userId, taskId);
    }

    @Override
    public TaskDto createTask(CreateTask createTask) {
        // Implementation for creating a task based on the request object
        Task task = new Task().builder()
                .content(createTask.getContent())
                .status(createTask.getStatus())
                .rescueTeam(rescueTeamRepository.findById(createTask.getRescueTeamId()).orElse(null))
                .build();
        return TaskDto.fromEntity(taskRepository.save(task));
    }

    public TaskStatsDTO getStats() {
        long completed = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long inProgress = taskRepository.countByStatus(TaskStatus.AGIND);
        long pending = taskRepository.countByStatus(TaskStatus.PENDING);
        Double avgMinutes = taskRepository.findAvgCompletionMinutesNative();

        return TaskStatsDTO.builder()
                .completedCount(completed)
                .inProgressCount(inProgress)
                .pendingCount(pending)
                .avgCompletionMinutes(avgMinutes)
                .build();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTaskStatusChanged(TaskStatusChangedEvent event) {
        TaskStatsDTO stats = getStats();
        // Push tới tất cả client đang subscribe kênh thống kê
        messagingTemplate.convertAndSend("/topic/dashboard/stats", stats);
    }

    @Override
    public TaskDto updateTaskStatus(Long taskId, TaskStatus status) {
        Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (task.getUser() == null || !task.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this task");
        }
        if (status == TaskStatus.COMPLETED) {
            task.setCompleteAt(java.time.LocalDateTime.now());
        }
        task.setStatus(status);
        task.setUpdateAt(java.time.LocalDateTime.now());
        return TaskDto.fromEntity(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> getTaskByUserId(){
        Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        return taskRepository.findByUserId(userId)
                .stream()
                .map(TaskDto::fromEntity)
                .toList();
    }

    @Override
    public List<TaskDto> getTaskByRescueTeamId(Long rescueTeamId) {
        return taskRepository.findByRescueTeamId(rescueTeamId)
                .stream()
                .map(TaskDto::fromEntity)
                .toList();
    }

}
