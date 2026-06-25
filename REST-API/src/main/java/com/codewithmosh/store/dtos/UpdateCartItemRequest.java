package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than zero")
    @Max(value = 1000, message = "Quantity less than or equal to 1000")
    private Integer quantity; //we don't use primitive int type because it is default non-nullable so we cant provide a not null message
}
