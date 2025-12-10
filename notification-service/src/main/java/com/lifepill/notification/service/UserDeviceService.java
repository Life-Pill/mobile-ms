package com.lifepill.notification.service;

import com.lifepill.notification.dto.DeviceRegistrationRequest;
import com.lifepill.notification.dto.DeviceRegistrationResponse;
import com.lifepill.notification.entity.UserDevice;
import com.lifepill.notification.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeviceService {
    
    private final UserDeviceRepository userDeviceRepository;
    
    /**
     * Register or update a device for push notifications.
     */
    @Transactional
    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest request) {
        log.info("Registering device for user: {}, deviceId: {}", request.getUserId(), request.getDeviceId());
        
        // Check if device already exists
        UserDevice device = userDeviceRepository.findByDeviceId(request.getDeviceId())
                .orElse(null);
        
        if (device != null) {
            // Update existing device
            device.setFcmToken(request.getFcmToken());
            device.setUserId(request.getUserId());
            device.setDeviceType(request.getDeviceType());
            device.setDeviceName(request.getDeviceName());
            device.setIsActive(true);
            log.info("Updated existing device: {}", device.getDeviceId());
        } else {
            // Create new device
            device = UserDevice.builder()
                    .userId(request.getUserId())
                    .fcmToken(request.getFcmToken())
                    .deviceType(request.getDeviceType())
                    .deviceId(request.getDeviceId())
                    .deviceName(request.getDeviceName())
                    .isActive(true)
                    .build();
            log.info("Created new device: {}", device.getDeviceId());
        }
        
        device = userDeviceRepository.save(device);
        
        return DeviceRegistrationResponse.builder()
                .id(device.getId())
                .userId(device.getUserId())
                .deviceId(device.getDeviceId())
                .deviceType(device.getDeviceType().name())
                .deviceName(device.getDeviceName())
                .registered(true)
                .build();
    }
    
    /**
     * Unregister a device (soft delete by setting inactive).
     */
    @Transactional
    public void unregisterDevice(String deviceId) {
        log.info("Unregistering device: {}", deviceId);
        
        userDeviceRepository.findByDeviceId(deviceId)
                .ifPresent(device -> {
                    device.setIsActive(false);
                    userDeviceRepository.save(device);
                    log.info("Device {} marked as inactive", deviceId);
                });
    }
    
    /**
     * Get all FCM tokens for a user.
     */
    public List<String> getFcmTokensForUser(UUID userId) {
        return userDeviceRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(UserDevice::getFcmToken)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active devices for a user.
     */
    public List<UserDevice> getDevicesForUser(UUID userId) {
        return userDeviceRepository.findByUserIdAndIsActiveTrue(userId);
    }
}
