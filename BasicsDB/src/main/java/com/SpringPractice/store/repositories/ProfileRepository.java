package com.SpringPractice.store.repositories;

import com.SpringPractice.store.dtos.UserSummary;
import com.SpringPractice.store.entities.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    //derived query method to find profiles with loyalty points greater than a certain value and sort by email
    @EntityGraph(attributePaths = "user")//eager load user with profile efficiently for this query to avoid n+1 problem
    //tricky because we are sorting by a property of a related entity (email from user needs to be loaded creating n+1)
    List<Profile> findByLoyaltyPointsGreaterThanOrderByUserEmail(int  loyaltyPoints);

    //using JPA buddy to extract custom JPQL query (alt + enter) to shorten the name
    //we need to select individual fields (id, email) because profile doesn't have email field
    //we also need "as" keyword because the dto object won't have the correct alias fields to populate getId and getEmail
    @Query("select p.id as id, p.user.email as email from Profile p where p.loyaltyPoints > :loyaltyPoints order by p.user.email")
    @EntityGraph(attributePaths = "user")
    List<UserSummary> findLoyalProfiles(@Param("loyaltyPoints") int  loyaltyPoints); //using dto interfance
    //because this method now represents user data rather than profile, it should be moved to the UserRepository
}