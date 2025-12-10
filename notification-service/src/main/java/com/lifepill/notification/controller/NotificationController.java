package com.lifepill.notification.controller;

import com.lifepill.notification.entity.Notification;
import com.lifepill.notification.service.FcmService;
import com.lifepill.notification.service.NotificationPersistenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification API", description = "Endpoints for notification management and WebSocket info")
public class NotificationController {

    private final NotificationPersistenceService persistenceService;
    private final FcmService fcmService;

    @Operation(summary = "Check notification service health/status")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "Notification Service");
        status.put("status", "running");
        status.put("timestamp", LocalDateTime.now().toString());
        status.put("webSocketEnabled", true);
        status.put("rabbitMQConnected", true);
        status.put("fcmEnabled", fcmService.isAvailable());
        status.put("databaseConnected", true);
        return ResponseEntity.ok(status);
    }
    
    @Operation(summary = "Get WebSocket connection information",
            description = "Returns WebSocket endpoint details for client connection")
    @GetMapping("/websocket-info")
    public ResponseEntity<Map<String, Object>> getWebSocketInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("wsEndpoint", "/ws");
        info.put("sockJSEnabled", true);
        info.put("jwtAuthEnabled", true);
        info.put("topics", Map.of(
                "allPrescriptions", "/topic/prescriptions",
                "branchPrescriptions", "/topic/branch/{branchId}/prescriptions",
                "userResponses", "/topic/user/{userId}/responses",
                "userNotifications", "/topic/user/{userId}/notifications"
        ));
        info.put("sendDestinations", Map.of(
                "markViewed", "/app/prescription/viewed"
        ));
        info.put("authHeader", "Authorization: Bearer <JWT_TOKEN>");
        return ResponseEntity.ok(info);
    }
    
    @Operation(summary = "Mark prescription as viewed by POS",
            description = "POS system calls this when a pharmacist views a prescription notification")
    @PostMapping("/prescriptions/{prescriptionId}/viewed")
    public ResponseEntity<Map<String, Object>> markPrescriptionViewed(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId,
            @RequestParam @Parameter(description = "Branch ID") UUID branchId,
            @RequestParam @Parameter(description = "Employee ID") UUID employeeId) {
        
        log.info("Prescription {} marked as viewed by employee {} at branch {}", 
                prescriptionId, employeeId, branchId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("prescriptionId", prescriptionId);
        response.put("branchId", branchId);
        response.put("employeeId", employeeId);
        response.put("viewedAt", LocalDateTime.now().toString());
        response.put("status", "VIEWED");
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get notification history for a user",
            description = "Returns paginated list of notifications sent to a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserNotifications(
            @PathVariable @Parameter(description = "User ID") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching notifications for user {} (page: {}, size: {})", userId, page, size);
        
        Page<Notification> notificationsPage = persistenceService.getUserNotifications(userId, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("notifications", notificationsPage.getContent());
        response.put("page", page);
        response.put("size", size);
        response.put("totalElements", notificationsPage.getTotalElements());
        response.put("totalPages", notificationsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get notification count for a user",
            description = "Returns unread notification count for badge display")
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Object>> getUserNotificationCount(
            @PathVariable @Parameter(description = "User ID") UUID userId) {
        
        Map<String, Long> counts = persistenceService.getNotificationCounts(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("unreadCount", counts.get("unreadCount"));
        response.put("totalCount", counts.get("totalCount"));
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Mark a notification as read")
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markAsRead(
            @PathVariable @Parameter(description = "Notification ID") UUID notificationId) {
        
        boolean success = persistenceService.markAsRead(notificationId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("notificationId", notificationId);
        response.put("success", success);
        response.put("readAt", success ? LocalDateTime.now().toString() : null);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Mark all notifications as read for a user")
    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @PathVariable @Parameter(description = "User ID") UUID userId) {
        
        int count = persistenceService.markAllAsRead(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("markedAsRead", count);
        response.put("readAt", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}

