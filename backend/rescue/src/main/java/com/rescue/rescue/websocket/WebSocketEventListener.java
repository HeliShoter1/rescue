package com.rescue.rescue.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String ONLINE_USERS_KEY = "online_users";

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            String userId = accessor.getUser().getName();
            accessor.getSessionAttributes().put("userId", userId);
            redisTemplate.opsForSet().add(ONLINE_USERS_KEY, userId);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        // Đọc từ session attributes thay vì native header
        String userId = (String) accessor.getSessionAttributes().get("userId");
        if (userId != null) {
            redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, userId);
        }
    }

    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForSet().isMember(ONLINE_USERS_KEY, userId.toString())
        );
    }
}