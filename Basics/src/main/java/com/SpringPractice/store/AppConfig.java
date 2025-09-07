package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PayPal;
import com.SpringPractice.store.PaymentService.PaymentService;
import com.SpringPractice.store.PaymentService.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${payment-service:stripe}")
    private String paymentService;

    @Bean
    public PaymentService stripe(){
        // if......
        return new Stripe();
    }

    @Bean
    public PaymentService paypal(){
        return new PayPal();
    }

    @Bean
    public OrderService orderService(){
        if (paymentService.equals("stripe")){
            return new OrderService(stripe());
        }
        return new OrderService(paypal());
    }
}
