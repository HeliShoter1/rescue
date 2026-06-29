package com.rescue.rescue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.TaskStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreateTask;
import com.rescue.rescue.service.Task.ITaskServide;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/tasks")
@AllArgsConstructor
public class TaskController {
    
    private final ITaskServide taskService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getTasksByUserId() {
        return ResponseEntity.ok(new ApiResponse("success", taskService.getTaskByUserId()));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(new ApiResponse("success", taskService.getTaskById(taskId)));
    }

    @GetMapping("/{rescueId}/rescueteam")
    public ResponseEntity<ApiResponse> getTaskByRescueTeam(@PathVariable Long rescueId) {
        return ResponseEntity.ok(new ApiResponse("success", taskService.getTaskByRescueTeamId(rescueId)));
    }

    @PostMapping("/{taskId}/register")
    public ResponseEntity<ApiResponse> registerTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(new ApiResponse("success", taskService.registerTask(taskId)));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTask(@RequestBody CreateTask task) {
        return ResponseEntity.ok(new ApiResponse("success", taskService.createTask(task)));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse> updateTaskStatus(@PathVariable Long taskId, @PathParam("status") TaskStatus status) {
        return ResponseEntity.ok(new ApiResponse("success", taskService.updateTaskStatus(taskId, status)));
    }
    
}
