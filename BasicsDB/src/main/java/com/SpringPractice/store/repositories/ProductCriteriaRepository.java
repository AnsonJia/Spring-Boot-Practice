package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Product;

import java.math.BigDecimal;
import java.util.List;

//interface for dynamic query by criteria api
public interface ProductCriteriaRepository {
    // we could add a Criteria object that encapsulate a bunch of values, or we can add multiple parameters
    List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);
}
