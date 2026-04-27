package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PaymentService;
import com.SpringPractice.store.PaymentService.Stripe;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//tells spring to manage this class
//@Component //general purpose annotation (often for utility classes)
//@Service // for classes that contain business logic (does the same thing as component)
// commented out annotation so we can configure beans using code (conditionally or 3rd party lib) AppConfig.java
public class OrderService {
    private PaymentService paymentService;

    //constructor
//    @Autowired //(used in older codebases or multiple constructors)
    //quailifier passes a specific implementation to be used
    public OrderService(/*@Qualifier("stripe")*/ PaymentService paymentService){
        this.paymentService=paymentService;
        //early initialization (potentially taxing if large)
        System.out.println("OrderService created"); // it will print this even when not called in
    }

    //will be called after the bean is initialized (allows for us to open db connections, network, anything after creation)
    @PostConstruct // tells spring there is some initialization to do after constructor
    public void init(){
        System.out.println("OrderService PostConstruct");
    }
    //gives us an opportunity to do things before destruction like release resources for db network etc
    @PreDestroy // tells spring to do these before destruction
    public void cleanup(){
        System.out.println("OrderService PreDestroy");
    }

    public void placeOrder(){
        paymentService.processPayment(10);
    }

    // setter injection (not commonly used) also not needed for setter
//    public void setPaymentService(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
}
