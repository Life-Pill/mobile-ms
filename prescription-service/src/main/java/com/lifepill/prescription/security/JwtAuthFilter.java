package com.lifepill.prescription.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * JWT Authentication Filter for validating user-auth tokens.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                
                // Check if this is an employee token or mobile user token
                if (jwtService.isEmployeeToken(jwt)) {
                    // Employee/POS token from Employee Identity Service
                    String email = jwtService.extractSubject(jwt);
                    
                    // Get roles from token or default to EMPLOYEE role
                    List<SimpleGrantedAuthority> authorities = jwtService.extractRoles(jwt);
                    if (authorities.isEmpty()) {
                        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                    }
                    
                    // Create authentication with roles from token
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Add employee info to request attributes if available
                    Long employerId = jwtService.extractEmployerId(jwt);
                    if (employerId != null) {
                        request.setAttribute("employerId", employerId);
                    }
                    request.setAttribute("employerEmail", email);

                    log.debug("Set authentication for employee: {} with roles: {}", email, authorities);
                } else {
                    // Mobile user token from user-auth service
                    UUID userId = jwtService.extractUserId(jwt);
                    String email = jwtService.extractEmail(jwt);

                    // Create authentication with USER role
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId.toString(),
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Add user info to request attributes for controllers
                    request.setAttribute("userId", userId);
                    request.setAttribute("userEmail", email);

                    log.debug("Set authentication for user: {}", userId);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
