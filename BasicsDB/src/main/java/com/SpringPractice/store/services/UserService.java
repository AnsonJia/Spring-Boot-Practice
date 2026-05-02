package com.SpringPractice.store.services;

import com.SpringPractice.store.entities.User;
import com.SpringPractice.store.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager; //responsible for managing entities using persistence context

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
}
