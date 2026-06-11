package com.rescue.rescue.service.Notification;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.MessageQueue.notification.NotificationProducer;
import com.rescue.rescue.enums.NotificationStatus;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.exceptions.UserDisabledException;
import com.rescue.rescue.model.Notification;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.NotificationsRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.sercurity.user.RescueUserDetail;
import com.rescue.rescue.websocket.WebSocketEventListener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;
    private final NotificationsRepository notificationRepository;
    private final UserReponsitory userRepository;
    private final NotificationProducer notificationProducer;

    @Override
    public List<Notification> getNotificationsByUserId(Long userId, Long cursor, Integer limit) {
        return notificationRepository.findByUserId(userId, cursor, limit);
    }

    @Override
    public Optional<Notification> getNotificationById(Long notificationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if(!notification.getUser().getId().equals(userId)) {
            throw new UserDisabledException("Notification not found with id: " + notificationId);
        }
        if (notification == null) {
            throw new ResourceNotFoundException("Notification not found with id: " + notificationId);
        }
        return Optional.of(notification);
    }

    @Override
    @Transactional
    public void sendNotification(Long userId, String title, String content) {
        User user = userRepository.findById(userId).orElse(null);
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .status(NotificationStatus.UNREAD)
                .createAt(LocalDate.now())
                .build();
        notificationRepository.save(notification);

        if (webSocketEventListener.isOnline(userId)) {
            messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",  
                notification
            );
        }
    }

    @Override
    public void sendViaQueue(Long receiverId, Long senderId, String title, String content) {
        notificationProducer.send(receiverId, senderId, title, content);
    }

    public void updateNotification(Long userId) {
        notificationRepository.findByUserId(userId, 0L, Integer.MAX_VALUE).forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notificationRepository.save(notification);
        });
    }
}
