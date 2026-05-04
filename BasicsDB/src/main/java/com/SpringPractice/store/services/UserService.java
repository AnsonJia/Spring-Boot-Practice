package com.SpringPractice.store.services;

import com.SpringPractice.store.entities.Address;
import com.SpringPractice.store.entities.User;
import com.SpringPractice.store.repositories.AddressRepository;
import com.SpringPractice.store.repositories.ProfileRepository;
import com.SpringPractice.store.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final EntityManager entityManager; //responsible for managing entities using persistence context
    private final AddressRepository addressRepository;

    //example of managing transactions
    @Transactional//changes the transaction boundary to the entire showEntityStates method (persist until method end)
    public void showEntityStates(){
        var user = User.builder()
        		.name("John")
        		.email("john@gmail.com")
        		.password("password")
        		.build();
        //check to see if user is in the persistent state or not
        //no repository method has been called yet so transient (new entity in the transient state)
        if (entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");
        //repository methods are transactional (save, delete, etc). New transaction starts and ends when completed
        userRepository.save(user);//transaction is only active when method is executed. after complete, detach
        //it will be detached because persistence only exists while the transaction is active
        //with @Transactional, it will persist until the method ends so it will be persistent
        if (entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");
    }
    //check to see if eager or lazy loading (Eager: OneToOne, ManyToOne     Lazy: OneToMany, ManyToMany)
    public void showRelatedEntities(){
        var user = userRepository.findById(1L).orElseThrow();
        System.out.println(user.getEmail()); //still prints both user+profile even with lazy loading
    } //in one-to-one relationships, changing fetch strategy only affects owner, so it won't affect the user entity
    //one method to fix this is to remove OneToOne method in User
        // likely best because when fetch user info for authentication, we don't need profile information


    //example of lazy loading
    //user is lazy loaded (when profile transaction ends, cant get user associated to it)
    @Transactional//persistent stays so hibernate can track profile object
    public void showRelatedEntities2(){
        var profile = profileRepository.findById(1L).orElseThrow(); //transaction ends so no persistence
        System.out.println(profile.getBio()); //doesn't load user
        System.out.println(profile.getUser().getEmail());//loads profile+user
    }
    //another example of lazy loading
    public void fetchAddress(){
        //in Address, there is a relationship to user and user has a one-to-one relationship with profile
            //profile will also be eager loaded (address+user+profile) if no lazy loading
        var address = addressRepository.findById(1L).orElseThrow();
    }


    //example of persisting related entities
    public void persistRelated(){
        var user = User.builder()
                .name("John")
                .email("john@gmail.com")
                .password("password")
                .build();
        var address = Address.builder()
                .street("street")
                .city("city")
                .state("state")
                .zip("zip")
                .build();
        user.addAddress(address);
        userRepository.save(user); //only user will be saved not address (hibernate does not propagate to related entities)
        //addressRepository.save(address); //one way to fix this is to save the address separately
            //a better solution is to add cascade persist to the relationship in user so address is propagated
    }


    //example of deleting parent and related entities (user)
    public void deleteRelated(){
        //hibernate first tries to fetch entity with its relationship (no referential integrity violation - data remains in valid state)
        userRepository.deleteById(1L);
        //issue 1: User has a OneToOne relationship with profile and doesn't know what to do with profile if delete user
            //set cascade type to remove in the user relationship
            //many-to-many JOIN tables don't need cascading (hibernate assumes it's safe to delete)
        //issue 2: Address table foreign key doesn't have a delete action (if user delete with address, cant delete user)
            //could set to on delete cascade for db SQL migration, best to set cascade in the application in addresses relation
    }
    //example of deleting child entity (addresses)
    @Transactional
    public void deleteRelated2(){
        var user = userRepository.findById(1L).orElseThrow();//findById is transactional. line ends, transaction ends
        var address = user.getAddresses().getFirst();//lazy lading between user and address (need to fetch address but no persistence context)
        user.removeAddress(address);//removeAddress sets user to null (orphan entity), but the db table is set to not null
            //set the addresses relationship in User with orphan removal true
        userRepository.save(user);
    }
}
