package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PayPal;
import com.SpringPractice.store.PaymentService.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		//IOC container
		ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
		// Can get a bean for an object managed by spring
		var orderService = context.getBean(OrderService.class);

		var resource = context.getBean(HeavyResource.class);

		// Constructor injection
		//var orderService = new OrderService(new PayPal());

		//Setter injection (not commonly used)
		//orderService.setPaymentService(new PayPal());
		orderService.placeOrder();


		var manager = context.getBean(NotificationManager.class);
		manager.sendNotification("test message");
	}

}
