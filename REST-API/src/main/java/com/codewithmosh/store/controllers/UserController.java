package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserMapper userMapper;// add userMapper field

    // method: GET, POST, PUT, DELETE
    //@RequestMapping("/users") //default uses the GET method (GetMapping also works and is more consice/shorter)
    @GetMapping
    public Iterable<UserDto> getAllUsers() { //repository returns an Iterable object
        //return userRepository.findAll(); //exposes all the data to clients (use DTOs)
        //use the stream api to map user objects to user dtos
        return userRepository.findAll()
                .stream()
                //map user to user dto and convert to list (if we change user entity, we just need to update mapping)
                //.map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())) //manual mapping user to userDto can become repetitive
                //instead better to use mapper to dto using MapStruct (user -> userMapper.toDto(user))
                .map(userMapper::toDto)
                .toList();//get all users in database
    };
    //get user by their id given by the path variable/url param
    @GetMapping("/{id}")//because id is a param, we need {} and @PathVariable for the variable
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) { //response entity allows us to customize our responses
        var user =  userRepository.findById(id).orElse(null);
        if (user == null) {// if null, return status 404 not found
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND); //one method but more verbose and creates new ResponseEntity
            return ResponseEntity.notFound().build(); //static factory methods inside of response entity are cleaner
        }
        //if not null, return status 200 and the user
        //return new ResponseEntity<>(user, HttpStatus.OK);
        //return ResponseEntity.ok(user); //exposes all the data to clients (use DTOs)

        //var userDto = new UserDto(user.getId(), user.getName(), user.getEmail()); //first create userDto and put in response
        //return ResponseEntity.ok(userDto); //only exposes the dto fields

        // no need to create a userDto for mapper
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
