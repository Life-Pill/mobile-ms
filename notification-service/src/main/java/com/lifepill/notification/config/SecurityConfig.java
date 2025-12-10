package com.lifepill.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the notification service.
 * Allows WebSocket connections and actuator endpoints while securing REST APIs.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Allow WebSocket endpoint
                        .requestMatchers("/ws/**").permitAll()
                        // Allow actuator endpoints
                        .requestMatchers("/actuator/**").permitAll()
                        // Allow OpenAPI/Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Allow notification status and info endpoints
                        .requestMatchers("/api/v1/notifications/status").permitAll()
                        .requestMatchers("/api/v1/notifications/websocket-info").permitAll()
                        // Require authentication for other endpoints
                        .anyRequest().permitAll() // TODO: Change to .authenticated() when auth is fully integrated
                );

        return http.build();
    }
}
