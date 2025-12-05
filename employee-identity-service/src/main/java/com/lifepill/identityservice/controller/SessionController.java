package com.lifepill.identityservice.controller;

import com.lifepill.identityservice.dto.request.CachedAuthRequestDTO;
import com.lifepill.identityservice.dto.request.LogoutRequestDTO;
import com.lifepill.identityservice.dto.response.CachedSessionResponseDTO;
import com.lifepill.identityservice.service.SessionService;
import com.lifepill.identityservice.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for session management endpoints.
 * Handles temporary/permanent logout and cached session operations.
 */
@RestController
@RequestMapping("/lifepill/v1/session")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Session Management", description = "Employer session management endpoints")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/logout/temporary")
    @Operation(summary = "Temporary logout - keeps cache data, only updates DB status to inactive")
    public ResponseEntity<StandardResponse> temporaryLogout(
            @Valid @RequestBody LogoutRequestDTO request
    ) {
        log.info("Temporary logout request for: {}", request.getUsername());
        sessionService.temporaryLogout(request.getUsername());
        return ResponseEntity.ok(new StandardResponse(200, "Temporary logout successful - cache preserved for PIN re-login", null));
    }

    @PostMapping("/logout/permanent")
    @Operation(summary = "Permanent logout - removes cache data completely and sets DB status to inactive")
    public ResponseEntity<StandardResponse> permanentLogout(
            @Valid @RequestBody LogoutRequestDTO request
    ) {
        log.info("Permanent logout request for: {}", request.getUsername());
        sessionService.permanentLogout(request.getUsername());
        return ResponseEntity.ok(new StandardResponse(200, "Permanent logout successful - cache removed", null));
    }

    @PostMapping("/authenticate/cached")
    @Operation(summary = "Authenticate using cached session data with username and PIN")
    public ResponseEntity<StandardResponse> authenticateFromCache(
            @Valid @RequestBody CachedAuthRequestDTO request
    ) {
        log.info("Cache authentication request for: {}", request.getUsername());
        CachedSessionResponseDTO response = sessionService.authenticateFromCache(request.getUsername(), request.getPin());
        return ResponseEntity.ok(new StandardResponse(200, "Cache authentication successful", response));
    }

    @GetMapping("/get-cached-employer/email/{employerEmail}")
    @Operation(summary = "Get cached employer session by email")
    public ResponseEntity<StandardResponse> getCachedEmployerByEmail(
            @PathVariable String employerEmail
    ) {
        log.info("Get cached employer request for: {}", employerEmail);
        Optional<CachedSessionResponseDTO> cachedSession = sessionService.getCachedSession(employerEmail);
        
        if (cachedSession.isPresent()) {
            CachedSessionResponseDTO session = cachedSession.get();
            
            // Check if session is valid
            if (session.isRevoked()) {
                return ResponseEntity.ok(new StandardResponse(403, "Session has been revoked", null));
            }
            if (session.getExpiresAt() < System.currentTimeMillis()) {
                return ResponseEntity.ok(new StandardResponse(403, "Session has expired", null));
            }
            
            return ResponseEntity.ok(new StandardResponse(200, "Cached employer found", session));
        } else {
            return ResponseEntity.ok(new StandardResponse(404, "No cached session found for employer", null));
        }
    }

    @GetMapping("/get-all-cached-employers")
    @Operation(summary = "Get all cached employer sessions")
    public ResponseEntity<List<CachedSessionResponseDTO>> getAllCachedEmployers() {
        log.info("Get all cached employers request");
        List<CachedSessionResponseDTO> cachedEmployers = sessionService.getAllCachedEmployers();
        return ResponseEntity.ok(cachedEmployers);
    }

    @GetMapping("/check/{employerEmail}")
    @Operation(summary = "Check if employer session is cached and valid (not revoked, not expired)")
    public ResponseEntity<StandardResponse> checkSessionCached(
            @PathVariable String employerEmail
    ) {
        log.info("Check cached session for: {}", employerEmail);
        boolean isValid = sessionService.isSessionValid(employerEmail);
        boolean isCached = sessionService.isSessionCached(employerEmail);
        
        String message;
        if (!isCached) {
            message = "Session not cached";
        } else if (!isValid) {
            message = "Session cached but invalid (revoked or expired)";
        } else {
            message = "Session is cached and valid";
        }
        
        return ResponseEntity.ok(new StandardResponse(200, message, isValid));
    }
}
