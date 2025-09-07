package com.SpringPractice.store.PaymentService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service ("stripe")
@Primary // used to set the primary bean
public class Stripe implements PaymentService {
    @Override
    public void processPayment(double amount){
        System.out.println("STRIPE");
        System.out.println("Amount: "+ amount);
    }
}
