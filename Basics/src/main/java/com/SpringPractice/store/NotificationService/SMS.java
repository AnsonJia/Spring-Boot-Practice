package com.SpringPractice.store.NotificationService;

import org.springframework.stereotype.Service;

@Service ("sms")
public class SMS implements NotificationService{
    @Override
    public void send(String message, String recipientPhoneNum) {
        System.out.println("Sending SMS: " + message);
    }
}
