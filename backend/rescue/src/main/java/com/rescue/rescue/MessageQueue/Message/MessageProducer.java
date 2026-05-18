package com.rescue.rescue.MessageQueue.Message;

// thư viện: spring-rabbit, lombok
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.rescue.rescue.config.RabbitMQConfig;
import com.rescue.rescue.dto.MessageEvent;

@Service
@AllArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageEvent event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.MESSAGE_EXCHANGE,
            RabbitMQConfig.MESSAGE_ROUTING_KEY,
            event
        );
    }
}