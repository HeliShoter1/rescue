package com.rescue.rescue.service.Notification;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.rescue.rescue.enums.NotificationStatus;
import com.rescue.rescue.model.Notification;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.NotificationsRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.websocket.WebSocketEventListener;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;
    private final NotificationsRepository notificationRepository;
    private final UserReponsitory userRepository;

    public void sendNotification(Long userId, String title, String content) {
        Notification notification = new Notification();
        User user = userRepository.findById(userId).orElse(null);
        notification.setUser(user);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreateAt(LocalDate.now());
        notificationRepository.save(notification);

        // Chỉ push WebSocket nếu online
        if (webSocketEventListener.isOnline(userId)) {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    Map.of("title", title, "content", content)
            );
        }
    }

    public void updateNotification(Long userId) {
        // Cập nhật trạng thái của tất cả thông báo của user
        notificationRepository.findByUserId(userId, 0L, Integer.MAX_VALUE).forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notificationRepository.save(notification);
        });
    }
}
