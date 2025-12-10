package com.lifepill.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Firebase Cloud Messaging (FCM) service for sending push notifications
 * to mobile devices when users are offline.
 */
@Service
@Slf4j
public class FcmService {

    @Value("${fcm.credentials.path:#{null}}")
    private String credentialsPath;

    @Value("${fcm.enabled:false}")
    private boolean fcmEnabled;

    private boolean initialized = false;

    @PostConstruct
    public void initialize() {
        if (!fcmEnabled) {
            log.info("FCM is disabled. Push notifications will not be sent.");
            return;
        }

        if (credentialsPath == null || credentialsPath.isEmpty()) {
            log.warn("FCM credentials path not configured. Push notifications disabled.");
            return;
        }

        try {
            InputStream serviceAccount;
            
            // Try classpath first, then file system
            try {
                serviceAccount = new ClassPathResource(credentialsPath).getInputStream();
            } catch (IOException e) {
                serviceAccount = new FileInputStream(credentialsPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                initialized = true;
                log.info("Firebase initialized successfully");
            }
        } catch (IOException e) {
            log.error("Failed to initialize Firebase: {}", e.getMessage());
        }
    }

    /**
     * Send a push notification to a single device.
     */
    public String sendToDevice(String fcmToken, String title, String body, Map<String, String> data) {
        if (!initialized || !fcmEnabled) {
            log.debug("FCM not initialized. Skipping push notification.");
            return null;
        }

        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data != null ? data : Map.of())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setSound("default")
                                    .setClickAction("OPEN_NOTIFICATION")
                                    .build())
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setSound("default")
                                    .setBadge(1)
                                    .build())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM message sent successfully: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Send a push notification to multiple devices.
     */
    public BatchResponse sendToMultipleDevices(List<String> fcmTokens, String title, String body, Map<String, String> data) {
        if (!initialized || !fcmEnabled || fcmTokens == null || fcmTokens.isEmpty()) {
            log.debug("FCM not initialized or no tokens. Skipping push notification.");
            return null;
        }

        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(fcmTokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data != null ? data : Map.of())
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("FCM multicast sent: {} success, {} failure",
                    response.getSuccessCount(), response.getFailureCount());
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM multicast: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Send prescription response notification to user's device.
     */
    public void sendPrescriptionResponseNotification(String fcmToken, UUID prescriptionId, 
                                                      String branchName, String status) {
        String title = "Prescription Response Received";
        String body = String.format("%s has responded to your prescription: %s", branchName, status);
        
        Map<String, String> data = Map.of(
                "type", "PRESCRIPTION_RESPONSE",
                "prescriptionId", prescriptionId.toString(),
                "branchName", branchName,
                "status", status
        );

        sendToDevice(fcmToken, title, body, data);
    }

    /**
     * Send order status update notification.
     */
    public void sendOrderStatusNotification(String fcmToken, UUID orderId, String status, String message) {
        String title = "Order Update";
        String body = message;

        Map<String, String> data = Map.of(
                "type", "ORDER_STATUS",
                "orderId", orderId.toString(),
                "status", status
        );

        sendToDevice(fcmToken, title, body, data);
    }

    /**
     * Check if FCM is available.
     */
    public boolean isAvailable() {
        return initialized && fcmEnabled;
    }
}
