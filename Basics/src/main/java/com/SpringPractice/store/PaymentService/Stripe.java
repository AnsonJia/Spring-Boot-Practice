package com.SpringPractice.store.PaymentService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service ("stripe")
//@Primary // used to set the primary bean
public class Stripe implements PaymentService {
    @Value("${stripe.apiUrl}")
    private String apiUrl;

    @Value("${stripe.enabled}")
    private boolean enabled;

    @Value("${stripe.timeout:3000}") //3000ms default value timeout if not provided
    private int timeout;

    @Value("${stripe.supported-currencies}")
    private List<String> supportedCurrencies;

    @Override
    public void processPayment(double amount){
        System.out.println("STRIPE");
        System.out.println("API URL: " + apiUrl);
        System.out.println("Enabled: " + enabled);
        System.out.println("Timeout: " + timeout);
        System.out.println("Currencies: " + supportedCurrencies);

        System.out.println("Amount: "+ amount);

    }
}
