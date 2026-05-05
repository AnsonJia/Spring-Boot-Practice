package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}