package com.lifepill.branchservice.config;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for Branch Service with role-based access control.
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
                // Public endpoints (no auth required)
                .requestMatchers(
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/api-docs/**"
                ).permitAll()
                
                // Branch exists check - internal service call
                .requestMatchers("/lifepill/v1/branch/exists/**").permitAll()
                
                // Branch READ endpoints - CASHIER and above
                .requestMatchers(HttpMethod.GET, "/lifepill/v1/branch/**").hasAnyRole("OWNER", "MANAGER", "CASHIER")
                .requestMatchers(HttpMethod.GET, "/lifepill/v1/branch-summary/**").hasAnyRole("OWNER", "MANAGER", "CASHIER")
                
                // Branch WRITE endpoints - MANAGER and above
                .requestMatchers(HttpMethod.POST, "/lifepill/v1/branch/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/lifepill/v1/branch/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/lifepill/v1/branch/**").hasAnyRole("OWNER", "MANAGER")
                
                // Branch DELETE - OWNER only
                .requestMatchers(HttpMethod.DELETE, "/lifepill/v1/branch/**").hasRole("OWNER")
                
                // Employer READ - CASHIER and above
                .requestMatchers(HttpMethod.GET, "/lifepill/v1/employer/**").hasAnyRole("OWNER", "MANAGER", "CASHIER")
                
                // Employer WRITE - MANAGER and above
                .requestMatchers(HttpMethod.POST, "/lifepill/v1/employer/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/lifepill/v1/employer/**").hasAnyRole("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/lifepill/v1/employer/**").hasAnyRole("OWNER", "MANAGER")
                
                // Employer DELETE - OWNER only
                .requestMatchers(HttpMethod.DELETE, "/lifepill/v1/employer/**").hasRole("OWNER")
                
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
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
