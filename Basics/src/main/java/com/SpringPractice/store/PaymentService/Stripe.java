package com.SpringPractice.store.PaymentService;

public class Stripe implements PaymentService {
    @Override
    public void processPayment(double amount){
        System.out.println("STRIPE");
        System.out.println("Amount: "+ amount);
    }
}
