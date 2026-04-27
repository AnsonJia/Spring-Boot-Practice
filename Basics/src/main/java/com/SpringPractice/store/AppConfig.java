package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PayPal;
import com.SpringPractice.store.PaymentService.PaymentService;
import com.SpringPractice.store.PaymentService.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration //tells spring that it is a source of bean definitions
public class AppConfig {
    // gets payment service type from application yaml with a default of stripe
    @Value("${payment-service:stripe}")
    private String paymentService;

    //tells spring that this method is a bean producer
    @Bean
    //methods to create beans
    public PaymentService stripe(){ //dont call bean name verbs but nouns
        // if......    //(can have logic and control over bean creation)
        return new Stripe();
    }

    @Bean
    public PaymentService paypal(){
        return new PayPal();
    }

    @Bean
    //@Lazy //creates objects only when needed
    // prototype bean example (creates a new bean instance everytime it is requested from IOC)
    //@Scope("prototype") //useful for components that hold state or used temporarily
    public OrderService orderService(){
        if (paymentService.equals("stripe")){
            return new OrderService(stripe());
        }
        return new OrderService(paypal());
    }
}
