package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.User;
import org.springframework.data.repository.CrudRepository;

//crud repository has 2 generate type parameters (entity for which we need to create repo, type of primary key in entity)
public interface UserRepository extends CrudRepository<User, Long> {
}
