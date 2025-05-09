package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PayPal;
import com.SpringPractice.store.PaymentService.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		//SpringApplication.run(StoreApplication.class, args);
		var orderService = new OrderService();
		orderService.setPaymentService(new PayPal());
		orderService.placeOrder();
	}

}
