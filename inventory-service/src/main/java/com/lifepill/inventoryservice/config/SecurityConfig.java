package com.lifepill.inventoryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for Inventory Service with role-based access control.
 * Role hierarchy: OWNER > MANAGER > CASHIER > USER > OTHER
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;
    
    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}")
    private String allowedMethods;
    
    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/api-docs/**"
                ).permitAll()
                
                // Item READ - USER and above (mobile users can search medicines)
                .requestMatchers(HttpMethod.GET, "/lifepill/v1/item/**").hasAnyRole("OWNER", "MANAGER", "CASHIER", "USER")
                
                // Item WRITE - MANAGER and above
                .requestMatchers(HttpMethod.POST, "/lifepill/v1/item/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/lifepill/v1/item/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/lifepill/v1/item/**").hasAnyRole("OWNER", "MANAGER")
                
                // Item DELETE - OWNER only
                .requestMatchers(HttpMethod.DELETE, "/lifepill/v1/item/**").hasRole("OWNER")
                
                // Category READ - USER and above
                .requestMatchers(HttpMethod.GET, "/lifepill/v1/category/**").hasAnyRole("OWNER", "MANAGER", "CASHIER", "USER")
                
                // Category WRITE - MANAGER and above
                .requestMatchers(HttpMethod.POST, "/lifepill/v1/category/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/lifepill/v1/category/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/lifepill/v1/category/**").hasRole("OWNER")
                
                // Supplier - MANAGER and above only
                .requestMatchers("/lifepill/v1/supplier/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers("/lifepill/v1/supplier-company/**").hasAnyRole("OWNER", "MANAGER")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        configuration.setExposedHeaders(List.of("Authorization", "X-Response-Time", "X-Request-Id"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
