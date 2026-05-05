package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}