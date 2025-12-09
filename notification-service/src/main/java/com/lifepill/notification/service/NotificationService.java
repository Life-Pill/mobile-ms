package com.lifepill.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastPrescriptionUpload(Object prescriptionData) {
        log.info("Broadcasting prescription upload to all branches: {}", prescriptionData);
        // In a real app, you might target specific branches.
        // Here we verify the broadcast functionality.
        messagingTemplate.convertAndSend("/topic/prescriptions", prescriptionData);
    }
}
