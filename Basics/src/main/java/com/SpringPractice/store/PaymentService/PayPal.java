package com.SpringPractice.store.PaymentService;

import org.springframework.stereotype.Service;

//required for autowiring
//@Service("paypal") //parameter is to name the service
// commented out annotation so we can configure beans using code (conditionally or 3rd party lib) AppConfig.java
public class PayPal implements PaymentService{
    @Override
    public void processPayment(double amount) {
        System.out.println("PAYPAL");
        System.out.println("Amount: "+ amount);
    }
}
