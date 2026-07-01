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
import com.rescue.rescue.dto.NotificationDTO;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;
    private final NotificationsRepository notificationRepository;
    private final UserReponsitory userRepository;
    private final NotificationProducer notificationProducer;

    @Override
    public List<NotificationDTO> getNotificationsByUserId( Long cursor, Integer limit) {
        Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        List<NotificationDTO> notifications = notificationRepository.findByUserId(userId, cursor, limit).stream()
                .map(NotificationDTO::fromEntity)
                .toList();

        return notifications;
    }

    @Override
    public Optional<NotificationDTO> getNotificationById(Long notificationId) {
         Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        System.out.println(notification);
        if(!notification.getUser().getId().equals(userId)) {
            throw new UserDisabledException("Notification not found with id: " + notificationId);
        }
        return Optional.of(NotificationDTO.fromEntity(notification));
    }

    @Override
    public void notifyAllUser(List<User> users, Long senderId, String title, String content) {
        for (User user : users) {
            notificationProducer.send(user.getId(), senderId, title, content);
        }
    }

    @Override
    @Transactional
    public void sendNotification(Long userId, Long senderId, String title, String content) {
        User user = userRepository.findById(userId).orElse(null);
        User sender = userRepository.findById(senderId).orElse(null);
        Notification notification = Notification.builder()
                .user(user)
                .sender(sender)
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
                NotificationDTO.fromEntity(notification)
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
