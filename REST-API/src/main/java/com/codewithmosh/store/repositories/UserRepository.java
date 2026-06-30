package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//using JpaRepository so findAll returns a list
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email); //derived query method

    //derived query that returns a user or empty (Optional<> because may not find user)
    Optional<User> findByEmail(String email); // Optional<> also allows us to use .orElse(null)
}
