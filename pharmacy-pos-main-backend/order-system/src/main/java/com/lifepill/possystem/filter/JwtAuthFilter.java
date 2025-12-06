package com.lifepill.possystem.filter;

import com.lifepill.possystem.config.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Jwt auth filter.
 * Supports both direct JWT validation and trusting API Gateway pre-validation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal
            (@NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        log.debug("JwtAuthFilter: Processing request to: {}", path);
        
        // Check for pre-validated header from API Gateway
        String authValidated = request.getHeader("X-Auth-Validated");
        String authUser = request.getHeader("X-Auth-User");
        String authRoles = request.getHeader("X-Auth-Roles");
        
        // If already validated by API Gateway, trust the headers
        if ("true".equals(authValidated) && authUser != null) {
            log.debug("Request pre-validated by API Gateway for user: {}", authUser);
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (authRoles != null && !authRoles.isEmpty()) {
                for (String role : authRoles.split(",")) {
                    authorities.add(new SimpleGrantedAuthority(role.trim()));
                }
            }
            
            log.debug("Authorities from Gateway: {}", authorities);
            
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
        
        // Otherwise, validate JWT directly (for direct service calls)
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract jwt from the Authorization
        jwt = authHeader.substring(7);
        
        try {
            // Extract email from token
            email = jwtService.extractUsername(jwt);
            
            // If user is present and no authentication object in securityContext
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Try to load user details, but fall back to JWT-based auth if not found
                try {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (Exception e) {
                    // User not found in local DB - use JWT claims for auth
                    log.debug("User {} not found in local DB, using JWT claims for authentication", email);
                    List<String> roles = jwtService.extractRoles(jwt);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (roles != null) {
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority(role));
                        }
                    }
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("JWT validation error: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    //Verify if it is whitelisted path and if yes don't do anything
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/lifepill/v1/auth");
    }
}