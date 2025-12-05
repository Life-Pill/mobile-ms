package com.lifepill.identityservice.service;

import com.lifepill.identityservice.dto.response.CachedEmployerDTO;
import com.lifepill.identityservice.dto.response.CachedSessionResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for employer session management with Redis caching.
 */
public interface SessionService {

    /**
     * Caches employer session data after successful authentication.
     *
     * @param employerEmail The employer's email
     * @param accessToken The JWT access token
     * @param refreshToken The JWT refresh token
     * @return The cached employer data
     */
    CachedEmployerDTO cacheEmployerSession(String employerEmail, String accessToken, String refreshToken);

    /**
     * Gets cached employer session by email.
     *
     * @param employerEmail The employer's email
     * @return Optional containing cached employer data if exists
     */
    Optional<CachedEmployerDTO> getCachedEmployer(String employerEmail);

    /**
     * Gets all cached employer sessions in proper response format.
     *
     * @return List of all cached employer sessions with authenticationResponse and employerDetails
     */
    List<CachedSessionResponseDTO> getAllCachedEmployers();

    /**
     * Authenticates using cached session data with username and PIN.
     *
     * @param username The employer's email/username
     * @param pin The employer's PIN
     * @return The cached session response with authenticationResponse and employerDetails
     */
    CachedSessionResponseDTO authenticateFromCache(String username, Integer pin);

    /**
     * Performs temporary logout - removes from cache but keeps DB status.
     *
     * @param employerEmail The employer's email
     */
    void temporaryLogout(String employerEmail);

    /**
     * Performs permanent logout - sets session as revoked and updates DB status.
     *
     * @param employerEmail The employer's email
     */
    void permanentLogout(String employerEmail);

    /**
     * Updates the last activity timestamp for a cached session.
     *
     * @param employerEmail The employer's email
     */
    void updateLastActivity(String employerEmail);

    /**
     * Checks if an employer session is cached and valid (not revoked, not expired).
     *
     * @param employerEmail The employer's email
     * @return true if session is cached and valid
     */
    boolean isSessionCached(String employerEmail);

    /**
     * Validates if a session is valid (not revoked and not expired).
     *
     * @param employerEmail The employer's email
     * @return true if session is valid
     */
    boolean isSessionValid(String employerEmail);

    /**
     * Removes expired sessions from cache.
     */
    void cleanupExpiredSessions();

    /**
     * Gets cached session in proper response format.
     *
     * @param employerEmail The employer's email
     * @return Optional containing cached session response if exists
     */
    Optional<CachedSessionResponseDTO> getCachedSession(String employerEmail);
}
