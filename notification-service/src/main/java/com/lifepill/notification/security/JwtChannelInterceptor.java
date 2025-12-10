package com.lifepill.notification.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * WebSocket channel interceptor for JWT authentication.
 * Validates JWT token on CONNECT and stores user principal.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Extract token from header
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            
            if (authHeader == null || authHeader.isEmpty()) {
                // Try token query param (for SockJS)
                List<String> tokenHeader = accessor.getNativeHeader("token");
                if (tokenHeader != null && !tokenHeader.isEmpty()) {
                    authHeader = "Bearer " + tokenHeader.get(0);
                }
            }
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                try {
                    if (jwtService.isTokenValid(token)) {
                        UUID userId = jwtService.extractUserId(token);
                        String username = jwtService.extractUsername(token);
                        
                        // Create authentication token
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                userId != null ? userId.toString() : username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                        
                        accessor.setUser(auth);
                        log.info("WebSocket CONNECT authenticated for user: {}", userId != null ? userId : username);
                    } else {
                        log.warn("Invalid JWT token on WebSocket CONNECT");
                    }
                } catch (Exception e) {
                    log.error("JWT validation error on WebSocket CONNECT: {}", e.getMessage());
                }
            } else {
                log.debug("No Authorization header on WebSocket CONNECT - anonymous connection");
            }
        }
        
        return message;
    }
}
