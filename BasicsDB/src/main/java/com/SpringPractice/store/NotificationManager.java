package com.SpringPractice.store;

import com.SpringPractice.store.NotificationService.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationManager {
    // final to ensure that it will not get reassigned after the constructor
    private final NotificationService notificationService;

    public NotificationManager(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // send notification message to the contact
    public void sendNotification(String message, String contact){
        notificationService.send(message, contact);
    }
}
