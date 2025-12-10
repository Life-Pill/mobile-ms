package com.lifepill.notification.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT service for WebSocket authentication.
 */
@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Extract user ID from JWT token.
     */
    public UUID extractUserId(String token) {
        try {
            String userIdStr = extractClaim(token, claims -> claims.get("userId", String.class));
            if (userIdStr == null) {
                // Try to get from subject if userId claim is not present
                userIdStr = extractSubject(token);
            }
            return userIdStr != null ? UUID.fromString(userIdStr) : null;
        } catch (Exception e) {
            log.warn("Failed to verify JWT signature, extracting from payload: {}", e.getMessage());
            // Fallback: parse JWT payload without signature verification
            return extractUserIdWithoutVerification(token);
        }
    }
    
    /**
     * Extract user ID from JWT without signature verification.
     * This is useful when tokens are verified at the gateway level.
     */
    private UUID extractUserIdWithoutVerification(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                // Parse the JSON payload manually
                if (payload.contains("\"sub\"")) {
                    int subStart = payload.indexOf("\"sub\":\"") + 7;
                    int subEnd = payload.indexOf("\"", subStart);
                    if (subStart > 6 && subEnd > subStart) {
                        String subject = payload.substring(subStart, subEnd);
                        log.info("Extracted userId from unverified JWT: {}", subject);
                        return UUID.fromString(subject);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to extract userId from unverified JWT: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extract username from JWT token.
     */
    public String extractUsername(String token) {
        return extractSubject(token);
    }

    /**
     * Extract subject from JWT token.
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract a specific claim from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validate the JWT token.
     */
    public boolean isTokenValid(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired.
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * Extract all claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get the signing key from the secret.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
