package com.SpringPractice.store.repositories.specifications;

import com.SpringPractice.store.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpec {
    //static utility methods for creating specifications
    //Specification interface has only 1 method which makes it a functional interface
    //takes 3 params (root, criteria query, criteria builder) and returns a predicate (condition for filtering)

    //filtering products by name
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }
    //filtering products by price range >=
    public static Specification<Product> hasPriceGreaterThanOrEqualTo(BigDecimal price) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), price);
    }
    //filtering products by price range <=
    public static Specification<Product> hasPriceLessThanOrEqualTo(BigDecimal price) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price);
    }
}
