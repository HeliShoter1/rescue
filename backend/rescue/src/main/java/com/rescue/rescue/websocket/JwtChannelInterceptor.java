package com.rescue.rescue.websocket;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.rescue.rescue.sercurity.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    if (jwtUtils.validateToken(token)) {
                        Long userId = jwtUtils.getIdFromToken(token);

                        Principal principal = new Principal() {
                            @Override
                            public String getName() {
                                return userId.toString();
                            }
                        };

                        accessor.setUser(principal);
                    }
                } catch (Exception e) {
                    log.warn("Invalid JWT on WebSocket CONNECT: {}", e.getMessage());
                }
            }
        }

        return message;
    }
}