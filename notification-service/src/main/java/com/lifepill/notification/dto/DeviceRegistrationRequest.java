package com.lifepill.notification.dto;

import com.lifepill.notification.entity.UserDevice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRegistrationRequest {
    
    @NotBlank(message = "User ID is required")
    private UUID userId;
    
    @NotBlank(message = "FCM token is required")
    private String fcmToken;
    
    @NotNull(message = "Device type is required")
    private UserDevice.DeviceType deviceType;
    
    @NotBlank(message = "Device ID is required")
    private String deviceId;
    
    private String deviceName;
}
