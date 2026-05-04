package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}