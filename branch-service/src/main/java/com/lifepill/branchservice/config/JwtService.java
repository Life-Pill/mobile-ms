package com.lifepill.branchservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * JWT Service for token validation.
 * Validates tokens issued by the Identity Service.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        
        // Check if this is a user-auth token (has "type" claim with "access" value)
        String tokenType = claims.get("type", String.class);
        if ("access".equals(tokenType)) {
            // This is a mobile user token from user-auth - assign USER role
            return List.of("ROLE_USER");
        }
        
        // Otherwise, extract roles from employee token
        List<String> roles = claims.get("roles", List.class);
        return roles != null ? roles : List.of();
    }

    /**
     * Extract employee ID from JWT token (for POS users).
     * Employee tokens include employerId claim.
     */
    public Long extractEmployerId(String token) {
        Claims claims = extractAllClaims(token);
        Object employerId = claims.get("employerId");
        if (employerId instanceof Number) {
            return ((Number) employerId).longValue();
        }
        return null;
    }

    /**
     * Extract branch ID from JWT token (for POS users).
     * Employee tokens include branchId claim.
     */
    public Long extractBranchId(String token) {
        Claims claims = extractAllClaims(token);
        Object branchId = claims.get("branchId");
        if (branchId instanceof Number) {
            return ((Number) branchId).longValue();
        }
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
