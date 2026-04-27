package com.SpringPractice.store;

import com.SpringPractice.store.entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		//ApplicationContext context = SpringApplication.run(StoreApplication.class, args);

		//example of using constructor to initialize all fields
		//var user = new User(1L, "name", "email", "password");

		//example of using getters and setters
		//var user2 = new User();
		//user2.setName("John");
		//user2.setEmail("john@example.com");
		//user2.setPassword("password");

		//example of using builder
		//helps to create complex obj step by step especially if there are lots of optional fields of config
		var user = User.builder()
				.name("John")
				.password("1234")
				.email("john@example.com")
				.build();

	}

}
