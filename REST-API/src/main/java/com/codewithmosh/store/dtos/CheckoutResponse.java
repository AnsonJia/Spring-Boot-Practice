package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class CheckoutResponse {//response dto after checkout
    private long orderId;//temporary response

    public CheckoutResponse(long orderId) {
        this.orderId = orderId;
    }
}
