package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//create entities if db tables exists using jpa buddy (rmb entities, new, JPA entities from db, select tables, check references, generate)
//with database first workflow, entities are cleaner because we don't need database schema annotations (@OnDelete, @ColumnDefault, etc)
@Getter //we don't use @Data for entities because it also includes @toString which can cause issues with lazy loaded fields
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")//, nullable = false) //don't need nullable because it's defined in database schema
    private Long id;

    //@NotNull //we add validation attributes to dtos not entities
    @ManyToOne//(fetch = FetchType.LAZY, optional = false) //relationships with one entity on one end, better use eager
    //@OnDelete(action = OnDeleteAction.CASCADE) //defined at database level
    @JoinColumn(name = "cart_id")//, nullable = false)
    private Cart cart;

    //@NotNull
    @ManyToOne//(fetch = FetchType.LAZY, optional = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id")//, nullable = false)
    private Product product;

    //@NotNull
    //@ColumnDefault("1") //defined in the database
    @Column(name = "quantity")//, nullable = false)
    private Integer quantity;


}