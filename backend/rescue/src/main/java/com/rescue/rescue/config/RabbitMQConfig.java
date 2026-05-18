package com.rescue.rescue.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_QUEUE    = "message.queue";
    public static final String MESSAGE_EXCHANGE = "message.exchange";
    public static final String MESSAGE_ROUTING_KEY = "message.routing.key";

    @Bean
    public Queue messageQueue() {
        return new Queue(MESSAGE_QUEUE, true); 
    }

    @Bean
    public DirectExchange messageExchange() {
        return new DirectExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public Binding messageBinding(Queue messageQueue, DirectExchange messageExchange) {
        return BindingBuilder
                .bind(messageQueue)
                .to(messageExchange)
                .with(MESSAGE_ROUTING_KEY);
    }

    // Dùng JSON thay vì Java serialization
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}