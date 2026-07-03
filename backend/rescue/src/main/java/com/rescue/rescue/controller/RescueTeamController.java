package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.RescueTeamStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreateRescueTeamRequest;
import com.rescue.rescue.request.UpdateRescueTeamRequest;
import com.rescue.rescue.service.RescueTeam.IRescueTeamService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("${api.prefix}/rescue-teams")
@AllArgsConstructor
public class RescueTeamController {
    private final IRescueTeamService rescueTeamService;

    @GetMapping("/rescue-team/{id}")
    public ResponseEntity<ApiResponse> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse("success", rescueTeamService.getRescueTeamById(id)));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> getRescueTeamByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(new ApiResponse("success", rescueTeamService.getRescueTeamByPostId(postId)));
    }

    @GetMapping("/rescueteams")
    public ResponseEntity<ApiResponse> getAllRescueTeams(@RequestParam  (value="cursor", required = true, defaultValue = "0") Long cursor,
                                                     @RequestParam(value = "limit", required = true, defaultValue = "10") Long limit) {
        return ResponseEntity.ok(new ApiResponse("success", rescueTeamService.getAllRescueTeam( cursor, limit)));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getAllRescueTeamByStatus(@RequestParam  (value="cursor", required = true, defaultValue = "0") Long cursor,
                                                     @RequestParam(value = "limit", required = true, defaultValue = "10") Integer limit,
                                                    @RequestParam(value = "status", required = true) RescueTeamStatus status ) {
        return ResponseEntity.ok(new ApiResponse("success", rescueTeamService.getAllRescueTeamsByStatus(status, cursor, limit)));
    }
    

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody CreateRescueTeamRequest entity) {
        //TODO: process POST request
        return ResponseEntity.ok(new ApiResponse("success", rescueTeamService.createRescueTeam(entity)));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> putMethodName( @RequestBody UpdateRescueTeamRequest entity) {
        //TODO: process PUT request
        rescueTeamService.updateRescueTeam(entity);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/{rescueTeamId}/assign/{postId}")
    public ResponseEntity<ApiResponse> assignRescueTeamToPost(@PathVariable Long rescueTeamId, @PathVariable Long postId) {
        rescueTeamService.assignRescueTeamToPost(rescueTeamId, postId);
        return ResponseEntity.ok(new ApiResponse("Rescue team assigned to post successfully", null));
    }
    
    
}
