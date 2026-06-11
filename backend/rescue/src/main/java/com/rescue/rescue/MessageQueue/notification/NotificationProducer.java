package com.rescue.rescue.MessageQueue.notification;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.rescue.rescue.config.RabbitMQConfig;
import com.rescue.rescue.dto.NotificationMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(Long receiverId, Long senderId, String title, String content) {
        NotificationMessage message = NotificationMessage.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .title(title)
                .content(content)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}