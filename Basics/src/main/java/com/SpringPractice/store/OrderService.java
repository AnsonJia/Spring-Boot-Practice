package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PaymentService;
import com.SpringPractice.store.PaymentService.Stripe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component //general purpose annotation (often for utility classes
//@Service // for classes that contain business logic (does the same thing as component)
public class OrderService {
    private PaymentService paymentService;

//    @Autowired //(used in older codebases or multiple constructors)
    public OrderService(/*@Qualifier("stripe")/* applies only */ PaymentService paymentService){
        this.paymentService=paymentService;
        System.out.println("OrderService created"); //early initialization (potentially taxing if large)
    }

    public void placeOrder(){
        paymentService.processPayment(10);
    }

//    public void setPaymentService(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
}
