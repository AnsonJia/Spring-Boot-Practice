package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

//we can gen repo with JPA buddy (rmb repositories, new, spring component, repository, name of file, parent set repo type)
public interface AddressRepository extends CrudRepository<Address, Long> {
}