package com.lifepill.inventoryservice.config;

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
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for Inventory Service.
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
        
        // Check for validated header from API Gateway
        String authValidated = request.getHeader("X-Auth-Validated");
        String authUser = request.getHeader("X-Auth-User");
        String authRoles = request.getHeader("X-Auth-Roles");
        
        // If already validated by API Gateway, trust the headers
        if ("true".equals(authValidated) && authUser != null) {
            log.debug("Request pre-validated by API Gateway for user: {}", authUser);
            
            List<SimpleGrantedAuthority> authorities = List.of();
            if (authRoles != null && !authRoles.isEmpty()) {
                authorities = List.of(authRoles.split(",")).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
            
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
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        
        try {
            if (jwtService.isTokenValid(jwt)) {
                String userEmail = jwtService.extractUsername(jwt);
                List<String> roles = jwtService.extractRoles(jwt);
                
                List<SimpleGrantedAuthority> authorities = List.of();
                if (roles != null) {
                    authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                log.debug("Authenticated user: {} with roles: {}", userEmail, roles);
            }
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
