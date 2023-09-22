package com.amigoscode.notification.controller;

import com.amigoscode.clients.NotificationRequest;
import com.amigoscode.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest notificationRequest){
        log.info("Request for sending notifications {}", notificationRequest);
        log.info( "customer email {}", notificationRequest.getCustomerEmail());
        log.info( "customer message {}", notificationRequest.getMessage());
        notificationService.send(notificationRequest);
    }
}
