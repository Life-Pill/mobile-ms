package com.lifepill.notification.repository;

import com.lifepill.notification.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    
    /**
     * Find all active devices for a user.
     */
    List<UserDevice> findByUserIdAndIsActiveTrue(UUID userId);
    
    /**
     * Find device by unique device ID.
     */
    Optional<UserDevice> findByDeviceId(String deviceId);
    
    /**
     * Find device by FCM token.
     */
    Optional<UserDevice> findByFcmToken(String fcmToken);
    
    /**
     * Check if device exists.
     */
    boolean existsByDeviceId(String deviceId);
    
    /**
     * Delete all devices for a user.
     */
    void deleteByUserId(UUID userId);
}
