package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//crud repository has 2 generate type parameters (entity for which we need to create repo, type of primary key in entity)
public interface UserRepository extends CrudRepository<User, Long> {
    //one to many or many to many use lazy loading, if we want to load tags with user, we can set fetch strategy to eager
    //but if we want to only load the tags for a particular query such as findByEmail, use entity graph
    //@EntityGraph(attributePaths = "tags")//set paths to name of entities we want to load for the query
    @EntityGraph(attributePaths = {"tags", "addresses"})//we can specify multiple paths to load multiple entities
    //@EntityGraph(attributePaths = {"tags", "addresses.country"})// we can also have nested relationships
    Optional<User> findByEmail(String email); //optional because we may not find the user
}
