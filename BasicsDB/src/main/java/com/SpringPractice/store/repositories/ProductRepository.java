package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    // String
    List<Product> findByName(String name);  //select * from products where name = ?
    List<Product> findByNameLike(String name); //select * from products where name like ?
    List<Product> findByNameNotLike(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameEndingWith(String name);
    List<Product> findByNameEndingWithIgnoreCase(String name);

    //Numbers
    List<Product> findByPrice(BigDecimal price);//select * from products where price = ?
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByPriceGreaterThanEqual(BigDecimal price);
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    //Null
    List<Product> findByDescriptionNull();
    List<Product> findByDescriptionNotNull();

    //Multiple Conditions
    List<Product> findByDescriptionNullAndNameNull();

    //Sort (OrderBy)
    List<Product> findByNameOrderByPrice(String name);
    List<Product> findByNameOrderByPriceAsc(String name);

    //Limit (Top/First)
    List<Product> findTop5ByNameOrderByPrice(String name);
    List<Product> findFirst5ByNameLikeOrderByPrice(String name);
}