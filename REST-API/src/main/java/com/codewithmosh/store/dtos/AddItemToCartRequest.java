package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {//request body for add item to cart endpoint
    @NotNull(message = "Product ID is required") //make sure productId is not null in request body
    private Long productId; //we only need to have productId in request
}
