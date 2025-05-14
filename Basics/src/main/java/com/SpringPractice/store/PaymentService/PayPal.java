package com.SpringPractice.store.PaymentService;

import org.springframework.stereotype.Service;

@Service
public class PayPal implements PaymentService{
    @Override
    public void processPayment(double amount) {
        System.out.println("PAYPAL");
        System.out.println("Amount: "+ amount);
    }
}
