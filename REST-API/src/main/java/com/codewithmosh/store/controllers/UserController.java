package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor//lombok constructor
@RequestMapping("/users") //mapping controller to the users endpoint to simplify url endpoint code
public class UserController {
    private final UserRepository userRepository;
    // method: GET, POST, PUT, DELETE
    //@RequestMapping("/users") //default uses the GET method (GetMapping also works and is more consice/shorter)
    @GetMapping
    public Iterable<User> getAllUsers() { //repository returns an Iterable object
        return userRepository.findAll();//get all users in database
    };
    //get user by their id given by the path variable/url param
    @GetMapping("/{id}")//because id is a param, we need {}
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
