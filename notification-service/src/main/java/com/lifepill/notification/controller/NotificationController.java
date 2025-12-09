package com.lifepill.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "Endpoints for notification management")
public class NotificationController {

    @Operation(summary = "Check notification service health/status")
    @GetMapping("/status")
    public String getStatus() {
        return "Notification Service is running and listening for events";
    }
}
