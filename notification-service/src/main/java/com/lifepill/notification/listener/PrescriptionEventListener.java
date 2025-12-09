package com.lifepill.notification.listener;

import com.lifepill.notification.config.RabbitMQConfig;
import com.lifepill.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrescriptionEventListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATION)
    public void handlePrescriptionUploaded(Object prescriptionData) {
        log.info("Received prescription upload event: {}", prescriptionData);
        notificationService.broadcastPrescriptionUpload(prescriptionData);
    }
}
