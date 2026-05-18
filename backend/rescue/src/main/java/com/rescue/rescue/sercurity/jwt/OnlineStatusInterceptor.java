package com.rescue.rescue.sercurity.jwt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.rescue.rescue.model.User;
import com.rescue.rescue.service.OnlineStatusService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OnlineStatusInterceptor implements HandlerInterceptor {

    private final OnlineStatusService onlineStatusService;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && 
            !(auth instanceof AnonymousAuthenticationToken)) {

            // Tùy theo bạn lưu gì trong Principal
            // Nếu Principal là UserDetails custom có getId()
            User user = (User) auth.getPrincipal();
            onlineStatusService.refreshTTL(user.getId());
        }

        return true;
    }
}
