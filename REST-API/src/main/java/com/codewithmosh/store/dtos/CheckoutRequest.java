package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {//request dto to checkout a cart
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
}
