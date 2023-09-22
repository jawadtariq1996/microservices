package com.amigoscode.notification.service;

import com.amigoscode.clients.NotificationRequest;
import com.amigoscode.notification.model.Notification;
import com.amigoscode.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(NotificationRequest request) {

        Notification notification = Notification.builder()
                .toCustomerId(request.getCustomerId())
                .sender("amigos")
                .toCustomerEmail(request.getCustomerEmail())
                .message(request.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

    }
}