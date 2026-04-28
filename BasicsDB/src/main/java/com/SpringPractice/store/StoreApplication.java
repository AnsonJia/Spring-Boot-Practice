package com.SpringPractice.store;

import com.SpringPractice.store.entities.Address;
import com.SpringPractice.store.entities.Tag;
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

		/*
		var address = Address.builder()
				.street("street")
				.city("city")
				.state("state")
				.zip("zip")
				.build();
		*/
		//example of adding an address/////////////////////////////////////////////////////////
		// Update the inverse side (the collection on User)
		// This does NOT affect the database, but keeps the in-memory object graph consistent
		//user.getAddresses().add(address);//user now knows about the addresses

		// Set the owning side of the relationship
		// This is the ONLY side that updates the database (sets address.user_id)
		//address.setUser(user); //address now knows about user
		//these 2 relations are highly related and required to run so it's best to put logic inside user class/////

		//replaces the 2 above lines with helper method inside User class
		//user.addAddress(address);

		var tag = new Tag("tag1");
		//wiring tag and user objects together, same as above
		//put below 2 in helper method user class
		//user.getTags().add(tag);
		//tag.getUsers().add(user);
		user.addTag("tag1");//replaces the lines above

		System.out.println(user);

	}

}
