package com.rescue.rescue.service.Message;

// thư viện: spring-context, spring-rabbit, spring-tx, lombok

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.rescue.rescue.model.Message;
import com.rescue.rescue.reponsitory.MessageRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.MessageQueue.Message.MessageProducer;
import com.rescue.rescue.dto.MessageEvent;
import com.rescue.rescue.enums.MessageStatus;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserReponsitory userRepository;
    private final MessageProducer messageProducer;

    @Override
    public void sendMessage(Long senderId, Long receiverId, String content) {
        // 1. Lưu message vào DB
        Message message = Message.builder()
                .sender(userRepository.getReferenceById(senderId))
                .receiver(userRepository.getReferenceById(receiverId))
                .content(content)
                .status(MessageStatus.SENT)
                .createAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        // 2. Đẩy event vào RabbitMQ — không block sender
        MessageEvent event = MessageEvent.builder()
                .messageId(message.getId())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .createAt(message.getCreateAt())
                .build();

        messageProducer.sendMessage(event);
    }
}
