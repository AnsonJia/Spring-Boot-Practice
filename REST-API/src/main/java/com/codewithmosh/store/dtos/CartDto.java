package com.codewithmosh.store.dtos;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private List<CartItemDto> items = new ArrayList<>(); //initialize the list to avoid so it won't be null in response (empty instead)
    private BigDecimal totalPrice = BigDecimal.ZERO; //BigDecimal is nullable so initialize to avoid null in response (0 instead)
}
