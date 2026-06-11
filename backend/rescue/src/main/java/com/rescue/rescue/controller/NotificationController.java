package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.service.Notification.INotificationService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("${api.prefix}/notifications")
@AllArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping("/{userId}/all")
    public ResponseEntity<ApiResponse> getNotifications(@PathVariable Long userId,
                                                        @RequestParam(value = "cursor", required = false, defaultValue = "0") Long cursor,
                                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return  ResponseEntity.ok(new ApiResponse("Place created", notificationService.getNotificationsByUserId(userId, cursor, limit)));
    }

    @GetMapping("/{notiId}/notification")
    public ResponseEntity<ApiResponse> getNotification(@PathVariable Long notiId) {
        return ResponseEntity.ok(new ApiResponse("Notification found", notificationService.getNotificationById(notiId)));
    }
}
