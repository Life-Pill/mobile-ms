package com.lifepill.prescription.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * JWT Service for validating tokens from user-auth service.
 * Uses the same secret key as user-auth for cross-service token validation.
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Validates a JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT token malformed: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT token invalid: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Extracts user ID from token (for mobile user tokens).
     */
    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    /**
     * Extracts email from token.
     */
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * Checks if this is an employee token (from Employee Identity Service).
     * Employee tokens don't have "type":"access" claim like mobile user tokens.
     */
    public boolean isEmployeeToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("type", String.class);
            // Mobile user tokens have type="access", employee tokens don't have this
            return tokenType == null || !tokenType.equals("access");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts employer ID from employee token.
     */
    public Long extractEmployerId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object employerId = claims.get("employerId");
            if (employerId instanceof Number) {
                return ((Number) employerId).longValue();
            }
        } catch (Exception e) {
            log.warn("Could not extract employerId from token: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extracts subject (email for employee, UUID for mobile user).
     */
    public String extractSubject(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    /**
     * Extracts roles from token (for POS/employee tokens).
     */
    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List) {
                List<String> roles = (List<String>) rolesObj;
                return roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase()))
                        .toList();
            }
        } catch (Exception e) {
            log.warn("Could not extract roles from token: {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Checks if token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
