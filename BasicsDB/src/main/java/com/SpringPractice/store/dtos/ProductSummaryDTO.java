package com.SpringPractice.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSummaryDTO {
    //classes allow us to encapsulate logic
    private Long id;
    private String name;
}
