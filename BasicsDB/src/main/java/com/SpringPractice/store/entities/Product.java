package com.SpringPractice.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    //example of doing a model first approach (create the model before the db) with JPA buddy
    //lmb on the name, alt enter, create flyway diff change, ok (default settings), set unwanted changed to ignored
    // to remove unwanted changes, highlight changes, click -, remove and ignore. make sure the name the file
    @Column(name = "description")//, /*length=1000*/ columnDefinition = "TEXT", nullable = false*/) //tags for db gen
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)//when product gets saved, category gets saved to
    @JoinColumn(name = "category_id")
    private Category category;
}
