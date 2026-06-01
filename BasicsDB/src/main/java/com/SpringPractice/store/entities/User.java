package com.SpringPractice.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//an entity is a java class that represents a table in our db. each instance represents a row in the db
@Setter //automatic generation of setters
@Getter //automatic generation of getters
@AllArgsConstructor //generates constructor to initialize all fields of the class
@NoArgsConstructor //allows us to generate a default empty constructions without any arg
//@toString// generates a to string method so output is readable to people (override toString for findByEmail in userRepo)
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
    //tells hibernate the owner of the relationship (name of field that is the owner of the relationship) ***required***
    //The relationship is controlled by the user field inside the Address class.
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
        //persist means related entity (address) also persisted
        //remove means if user removed, address also removed
        //orphanRemoval tells hibernate to remove orphan entities
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
        address.setUser(null);//sets user to null (orphan entity), but the db table is set to not null
    }

    public void addTag(String tagName) {//passing a string so the class is responsible for creating the tag object
        var tag = new Tag(tagName); //create a new tag object with the string name
        tags.add(tag);
        tag.getUsers().add(this);
    }

    public void removeTag(String tagName) {
        var tag = new Tag(tagName);
        tags.remove(tag);
        tag.getUsers().remove(this);
    }

    //many-to-many relation. a user can have many tags and a tags can belong to many users (we set user as owner here)
    @ManyToMany //in a many-to-many relation, either side can be the owner (both sides are foreign keys)
    @JoinTable( //we will have user be the owner (owner must have @JoinColumn but for n-n we use @JoinTable)
            name = "user_tags", //needs to include the name of join table (n-n relation needs a separate table)
            joinColumns = @JoinColumn(name = "user_id"), //set name of foreign key referencing the owner (Users) table
            inverseJoinColumns = @JoinColumn(name = "tag_id")//set name of the other foreign key
    )
    @Builder.Default //makes sure this gets initialized
    private Set<Tag> tags = new HashSet<>();

    //one-to-one relation. a user has one profile and every profile is mapped to one user
    //the owner is the name of the field in profile
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)//cascade remove to profile is user is removed
    private Profile profile;

    //lazy loading by default
    @ManyToMany//cannot set delete cascade otherwise if user delete, product delete (user is the owner)
    @JoinTable(//example using model first approach to build db with JPA buddy
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    //we don't need to go to product to add the inverse relation. Product doesn't need to know about users
    private Set<Product> wishlist = new HashSet<>();

    public void addFavouriteProduct(Product product) {//add products to the wishlist
        wishlist.add(product);//uni-directional relationship (product doesn't need to know about users)
    }

    @Override //override toString to only include the fields we want to print 
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
