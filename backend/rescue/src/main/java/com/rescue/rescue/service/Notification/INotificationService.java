package com.rescue.rescue.service.Notification;

import java.util.List;
import java.util.Optional;

import com.rescue.rescue.enums.NotificationStatus;
import com.rescue.rescue.model.Notification;

public interface INotificationService {
    void sendNotification(Long userId, Long senderId, String title, String content);
    void updateNotification(Long userId);
    List<Notification> getNotificationsByUserId(Long userId, Long cursor, Integer limit);
    Optional<Notification> getNotificationById(Long notificationId);
    void sendViaQueue(Long receiverId, Long senderId, String title, String content);
}
