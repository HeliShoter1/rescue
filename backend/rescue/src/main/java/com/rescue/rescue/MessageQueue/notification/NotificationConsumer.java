package com.rescue.rescue.MessageQueue.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rescue.rescue.config.RabbitMQConfig;
import com.rescue.rescue.dto.NotificationMessage;
import com.rescue.rescue.service.Notification.INotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final INotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(NotificationMessage message) {
        log.info("Received notification for userId={}", message.getReceiverId());
        notificationService.sendNotification(
                message.getReceiverId(),
                message.getTitle(),
                message.getContent()
        );
    }
}