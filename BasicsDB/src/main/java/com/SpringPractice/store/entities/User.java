package com.SpringPractice.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//an entity is a java class that represents a table in our db. each instance represents a row in the db
@Setter //automatic generation of setters
@Getter //automatic generation of getters
@AllArgsConstructor //generates constructor to initialize all fields of the class
@NoArgsConstructor //allows us to generate a default empty constructions without any arg
@ToString// generates a to string method so output is readable to people
@Builder //allows us to use builder to initialize an entity
@Entity //allows us to work with java objects rather than raw SQL
@Table(name = "users") //hibernate assumes we have a table called User so we need to set the name to "users"
public class User {
    //getters and setters (generate: alt + fn + insert  -> getters and setters -> shift down arrow for all fields)
    //to organize code, shift + ctrl + a -> search "rearrange code"
    @Id //tells hibernate that this is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tells hibernate that it's an auto increment
    @Column(name = "id")//hibernate assumes, make sure to match name to db col (best practice)
    private Long id;

    @Column(nullable = false, name = "name") // nullable is only needed if we let hib generate our db tables at runtime
    private String name;

    @Column(nullable = false, name = "email")//flyway defines the nullable attribute at the db level so we don't need
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    //defines a one-to-many relationship (a user can have many addresses but every address belongs to 1 user)
    // tells hibernate the owner of the relationship (name of field that is the owner of the relationship)
    @OneToMany(mappedBy = "user") //The relationship is controlled by the user field inside the Address class.
    @Builder.Default //tells builder to include initializations like this when building an object (don't use builder)
    private List<Address> addresses =  new ArrayList<>();

    //helper method for adding addresses
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    //helper method to remove address
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }
}
