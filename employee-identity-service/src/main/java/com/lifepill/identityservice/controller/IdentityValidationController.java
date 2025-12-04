package com.lifepill.identityservice.controller;

import com.lifepill.identityservice.dto.response.EmployerAuthDetailsResponseDTO;
import com.lifepill.identityservice.service.AuthService;
import com.lifepill.identityservice.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for identity validation endpoints used by other services.
 */
@RestController
@RequestMapping("/lifepill/v1/identity")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Identity Validation", description = "Token validation endpoints for microservices")
public class IdentityValidationController {

    private final AuthService authService;

    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token and return employer details")
    public ResponseEntity<StandardResponse> validateToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        log.info("Token validation request");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(new StandardResponse(400, "Invalid authorization header", null));
        }
        
        String token = authHeader.substring(7);
        EmployerAuthDetailsResponseDTO employerDetails = authService.validateToken(token);
        
        return ResponseEntity.ok(new StandardResponse(200, "Token is valid", employerDetails));
    }

    @GetMapping("/employer/{email}")
    @Operation(summary = "Get employer details by email (for internal service communication)")
    public ResponseEntity<StandardResponse> getEmployerByEmail(@PathVariable String email) {
        log.info("Get employer details for email: {}", email);
        EmployerAuthDetailsResponseDTO employerDetails = authService.getEmployerDetails(email);
        return ResponseEntity.ok(new StandardResponse(200, "Employer details retrieved", employerDetails));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Identity Service is healthy");
    }
}
