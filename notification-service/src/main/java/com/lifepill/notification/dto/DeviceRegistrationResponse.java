package com.lifepill.notification.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRegistrationResponse {
    
    private UUID id;
    private UUID userId;
    private String deviceId;
    private String deviceType;
    private String deviceName;
    private boolean registered;
}
