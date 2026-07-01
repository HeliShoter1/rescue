package com.rescue.rescue.service.Notification;

import java.util.List;
import java.util.Optional;

import com.rescue.rescue.dto.NotificationDTO;
import com.rescue.rescue.enums.NotificationStatus;
import com.rescue.rescue.model.Notification;
import com.rescue.rescue.model.User;

public interface INotificationService {
    void sendNotification(Long userId, Long senderId, String title, String content);
    void updateNotification(Long userId);
    List<NotificationDTO> getNotificationsByUserId( Long cursor, Integer limit);
    Optional<NotificationDTO> getNotificationById(Long notificationId);
    void sendViaQueue(Long receiverId, Long senderId, String title, String content);
    void notifyAllUser(List<User> users, Long senderId, String title, String content);
}
