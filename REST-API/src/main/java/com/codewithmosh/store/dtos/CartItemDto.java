package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto { //new dto for addToCart endpoint response
    private CartProductDto product; //separate dto for product details in cart item response (in case ProductDto changes)
    private int quantity;
    private BigDecimal totalPrice;

    /* ex response
    {
        "product": {
            "id": 1,
            "name": "Product 1",
            "price": 10.00
        },
        "quantity": 2,
        "totalPrice": 20.00
    }
     */

}
