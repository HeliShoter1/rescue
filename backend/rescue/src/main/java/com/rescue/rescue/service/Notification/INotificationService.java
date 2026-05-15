package com.rescue.rescue.service.Notification;

import com.rescue.rescue.enums.NotificationStatus;

public interface INotificationService {
    void sendNotification(Long userId, String title, String content);
    void updateNotification(Long userId);
}
