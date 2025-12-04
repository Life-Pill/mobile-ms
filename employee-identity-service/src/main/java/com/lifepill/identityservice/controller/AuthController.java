package com.lifepill.identityservice.controller;

import com.lifepill.identityservice.dto.request.AuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.PinAuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.RegisterRequestDTO;
import com.lifepill.identityservice.dto.response.AuthenticationResponseDTO;
import com.lifepill.identityservice.dto.response.EmployerAuthDetailsResponseDTO;
import com.lifepill.identityservice.service.AuthService;
import com.lifepill.identityservice.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/lifepill/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Employee authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new employer")
    public ResponseEntity<StandardResponse> register(
            @Valid @RequestBody RegisterRequestDTO registerRequest
    ) {
        log.info("Registration request for: {}", registerRequest.getEmployerEmail());
        AuthenticationResponseDTO authResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardResponse(201, "Registration successful", authResponse));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate with email and password")
    public ResponseEntity<StandardResponse> authenticate(
            @Valid @RequestBody AuthenticationRequestDTO request,
            HttpServletResponse response
    ) {
        log.info("Authentication request for: {}", request.getEmployerEmail());
        AuthenticationResponseDTO authResponse = authService.authenticate(request);
        EmployerAuthDetailsResponseDTO employerDetails = authService.getEmployerDetails(request.getEmployerEmail());

        // Set the token as a cookie
        Cookie cookie = new Cookie("Authorization", authResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("authenticationResponse", authResponse);
        responseData.put("employerDetails", employerDetails);

        return ResponseEntity.ok(new StandardResponse(200, "Authentication successful", responseData));
    }

    @PostMapping("/authenticate-pin")
    @Operation(summary = "Authenticate with email and PIN")
    public ResponseEntity<StandardResponse> authenticateWithPin(
            @Valid @RequestBody PinAuthenticationRequestDTO request,
            HttpServletResponse response
    ) {
        log.info("PIN authentication request for: {}", request.getEmployerEmail());
        AuthenticationResponseDTO authResponse = authService.authenticateWithPin(request);
        EmployerAuthDetailsResponseDTO employerDetails = authService.getEmployerDetails(request.getEmployerEmail());

        // Set the token as a cookie
        Cookie cookie = new Cookie("Authorization", authResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("authenticationResponse", authResponse);
        responseData.put("employerDetails", employerDetails);

        return ResponseEntity.ok(new StandardResponse(200, "PIN authentication successful", responseData));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and set user inactive")
    public ResponseEntity<StandardResponse> logout(
            @RequestParam String email,
            HttpServletResponse response
    ) {
        log.info("Logout request for: {}", email);
        authService.setActiveStatus(email, false);

        // Clear the cookie
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(new StandardResponse(200, "Logout successful", null));
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Identity Service is running");
    }
}
