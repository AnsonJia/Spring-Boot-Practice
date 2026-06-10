package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor//lombok constructor
public class UserController {
    private final UserRepository userRepository;
    // method: GET, POST, PUT, DELETE
    //@RequestMapping("/users") //default uses the GET method
    @GetMapping("/users")//shorter but functionally identical
    public Iterable<User> getAllUsers() { //repository returns an Iterable object
        return userRepository.findAll();//get all users in database
    };
}
