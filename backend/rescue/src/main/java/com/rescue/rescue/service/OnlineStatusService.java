package com.rescue.rescue.service;

// thư viện: spring-data-redis, lombok
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OnlineStatusService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "online:user:";
    private static final long TTL_MINUTES = 30;

    // Gọi khi user login
    public void setOnline(Long userId) {
        redisTemplate.opsForValue().set(
            PREFIX + userId,
            "1",
            TTL_MINUTES,
            TimeUnit.MINUTES
        );
    }

    // Gọi khi user logout
    public void setOffline(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    // Kiểm tra trước khi push WebSocket
    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + userId));
    }

    // Refresh TTL mỗi khi user có hoạt động (gọi API, gửi tin...)
    public void refreshTTL(Long userId) {
        redisTemplate.expire(PREFIX + userId, TTL_MINUTES, TimeUnit.MINUTES);
    }
}
