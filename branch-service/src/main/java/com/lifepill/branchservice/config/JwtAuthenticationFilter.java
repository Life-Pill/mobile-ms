package com.lifepill.branchservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT Authentication Filter for Branch Service.
 * Validates JWT tokens and sets up Spring Security context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        log.info("JwtAuthenticationFilter: Processing request to: {}", path);
        
        // Check for validated header from API Gateway
        String authValidated = request.getHeader("X-Auth-Validated");
        String authUser = request.getHeader("X-Auth-User");
        String authRoles = request.getHeader("X-Auth-Roles");
        
        // If already validated by API Gateway, trust the headers
        if ("true".equals(authValidated) && authUser != null) {
            log.info("Request pre-validated by API Gateway for user: {}", authUser);
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (authRoles != null && !authRoles.isEmpty()) {
                for (String role : authRoles.split(",")) {
                    authorities.add(new SimpleGrantedAuthority(role.trim()));
                }
            }
            
            log.info("Authorities from Gateway: {}", authorities);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    authorities
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            filterChain.doFilter(request, response);
            return;
        }
        
        // Otherwise, validate JWT directly
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("No Bearer token found for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        log.info("JWT token found, attempting validation...");
        
        try {
            if (jwtService.isTokenValid(jwt)) {
                String userEmail = jwtService.extractUsername(jwt);
                List<String> roles = jwtService.extractRoles(jwt);
                
                log.info("JWT valid for user: {} with {} roles", userEmail, roles != null ? roles.size() : 0);
                
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) {
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }
                
                log.info("Setting authentication with authorities: {}", authorities);
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                log.info("Authentication set successfully for user: {}", userEmail);
            } else {
                log.warn("JWT token is invalid or expired");
            }
        } catch (Exception e) {
            log.error("JWT validation error: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
