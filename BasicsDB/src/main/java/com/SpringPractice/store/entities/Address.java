package com.SpringPractice.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip")
    private String zip;

    @Column(name = "state")
    private String state;

    // a user can have many addresses but each address belongs to 1 user
    @ManyToOne
    @JoinColumn(name = "user_id") //foreign key specification (owner of relation is usually one with foreign key)
    //Prevents infinite loops when printing (excludes the user from being converted to string)
    @ToString.Exclude  //when converting address to string, it reads the user field, in user, it references address
    private User user;
}
