package com.SpringPractice.store.NotificationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("email")
@Primary
public class Email implements NotificationService {
    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private String port;

    @Override
    public void send(String message, String recipientEmail) {
        System.out.println("Sending email: " + message);
        System.out.println("Message: " + message);
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
    }
}
