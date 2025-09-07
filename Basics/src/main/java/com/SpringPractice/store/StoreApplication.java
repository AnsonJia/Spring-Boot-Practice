package com.SpringPractice.store;

import com.SpringPractice.store.PaymentService.PayPal;
import com.SpringPractice.store.PaymentService.Stripe;
import com.SpringPractice.store.UserRegistrationService.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		//IOC container where beans are stored
		//ApplicationContext context = SpringApplication.run(StoreApplication.class, args);

		//gives us the ability to manually close the context
		ConfigurableApplicationContext context = SpringApplication.run(StoreApplication.class, args);

		// Can get a bean for an object managed by spring
		var orderService = context.getBean(OrderService.class);
		var orderService2 = context.getBean(OrderService.class); //singleton bean example (single instance default)

		var resource = context.getBean(HeavyResource.class);

		// Constructor injection
		//var orderService = new OrderService(new PayPal());

		//Setter injection (not commonly used)
		//orderService.setPaymentService(new PayPal());
		orderService.placeOrder();


		var manager = context.getBean(NotificationManager.class);
		manager.sendNotification("test message", "12345678");

		// create 2 identical users to demonstrate error
		var userService = context.getBean(UserService.class);
		userService.registerUser(new User(1L, "example@gmail.com", "12345678", "name"));
		userService.registerUser(new User(1L, "example@gmail.com", "12345678", "name"));


		context.close();
	}

}
