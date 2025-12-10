package com.lifepill.notification.controller;

import com.lifepill.notification.dto.DeviceRegistrationRequest;
import com.lifepill.notification.dto.DeviceRegistrationResponse;
import com.lifepill.notification.security.JwtService;
import com.lifepill.notification.service.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/lifepill/v1/notifications/devices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Device Registration", description = "Endpoints for managing FCM device tokens for push notifications")
public class UserDeviceController {
    
    private final UserDeviceService userDeviceService;
    private final JwtService jwtService;
    
    @Operation(summary = "Register device for push notifications",
            description = "Register or update a device's FCM token for receiving push notifications")
    @PostMapping("/register")
    public ResponseEntity<DeviceRegistrationResponse> registerDevice(
            @Valid @RequestBody DeviceRegistrationRequest request,
            HttpServletRequest httpRequest) {
        
        // Extract userId from JWT token
        UUID userId = extractUserIdFromRequest(httpRequest);
        request.setUserId(userId);
        
        log.info("Device registration request for user: {}", userId);
        DeviceRegistrationResponse response = userDeviceService.registerDevice(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    private UUID extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                UUID userId = jwtService.extractUserId(token);
                if (userId != null) {
                    log.info("Extracted userId from JWT: {}", userId);
                    return userId;
                }
                log.error("JwtService returned null userId for token");
            } catch (Exception e) {
                log.error("Failed to extract userId from JWT token: {}", e.getMessage());
            }
        } else {
            log.error("No Authorization header or invalid format");
        }
        throw new IllegalStateException("Unable to extract user ID from authentication token");
    }
    
    @Operation(summary = "Unregister device",
            description = "Unregister a device from receiving push notifications")
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Map<String, Object>> unregisterDevice(@PathVariable String deviceId) {
        
        log.info("Device unregistration request for deviceId: {}", deviceId);
        userDeviceService.unregisterDevice(deviceId);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Device unregistered successfully",
                "deviceId", deviceId
        ));
    }
}

