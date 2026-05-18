package com.rescue.rescue.MessageQueue.Message;

// thư viện: spring-rabbit, spring-websocket, spring-messaging, spring-data-redis, lombok

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.rescue.rescue.config.RabbitMQConfig;
import com.rescue.rescue.dto.MessageEvent;
import com.rescue.rescue.enums.MessageStatus;
import com.rescue.rescue.reponsitory.MessageRepository;
import com.rescue.rescue.service.OnlineStatusService;

@Slf4j
@Service
@AllArgsConstructor
public class MessageConsumer {

    private final SimpMessagingTemplate webSocket;
    private final OnlineStatusService onlineStatusService;
    private final MessageRepository messageRepository;

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE)
    public void handleMessage(MessageEvent event) {
        try {
            boolean isOnline = onlineStatusService.isOnline(event.getReceiverId());

            if (isOnline) {
                webSocket.convertAndSendToUser(
                    event.getReceiverId().toString(),
                    "/queue/messages",
                    event
                );
                log.info("Pushed message {} to user {}", event.getMessageId(), event.getReceiverId());
            } else {
                // Receiver offline → đánh dấu PENDING để gửi lại sau
                messageRepository.updateStatus(event.getMessageId(), MessageStatus.SENT);
                log.info("User {} offline, message {} marked as SENT", event.getReceiverId(), event.getMessageId());
            }

        } catch (Exception e) {
            log.error("Failed to handle message {}: {}", event.getMessageId(), e.getMessage(), e);
            // Ném lại để RabbitMQ retry hoặc đẩy vào Dead Letter Queue
            throw new RuntimeException("Message handling failed", e);
        }
    }
}
