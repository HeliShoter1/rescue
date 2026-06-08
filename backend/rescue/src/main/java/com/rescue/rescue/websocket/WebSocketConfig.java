package com.rescue.rescue.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");  // prefix subscribe
        registry.setApplicationDestinationPrefixes("/app"); // prefix gửi từ client
        registry.setUserDestinationPrefix("/user"); // prefix gửi đến user cụ thể
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(apiPrefix + "/ws")  // endpoint client kết nối
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
