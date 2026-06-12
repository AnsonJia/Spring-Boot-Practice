package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")//tells spring to load the category along with the product
    List<Product> findByCategoryId(Byte categoryId); //filter products by category

    @EntityGraph(attributePaths = "category")// to fetch the category along with the product
    @Query("SELECT p FROM Product p")// JOIN FETCH p.category")//JPQL query to select all products and their category
    List<Product> findAllWithCategory(); //find all products after joining the category with products
}