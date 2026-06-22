package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {//separate dto for product details in cart item response
    private Long id;
    private String name;
    private BigDecimal price;
}
