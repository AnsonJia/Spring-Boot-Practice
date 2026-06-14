package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor//lombok constructor
@RequestMapping("/users") //mapping controller to the users endpoint to simplify url endpoint code
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;// add userMapper field

    // method: GET, POST, PUT, DELETE
    //@RequestMapping("/users") //default uses the GET method (GetMapping also works and is more consice/shorter)
    @GetMapping
    public Iterable<UserDto> getAllUsers(
            //headers are often used for metadata (authentication, caching, etc.)
            //@RequestHeader(required = false, name = "x-auth-token") String authToken, //convention custom headers with x-

            //annotation for a query request param (set required to false for optional and if no sort field provided, default is empty string)
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy //name binds query parameter "sort" to method parameter sortBy
    ) { //repository returns an Iterable object

        //System.out.println(authToken);

        //check if sort field is valid, otherwise sort by name
        if (!Set.of("name", "email").contains(sortBy)) //because contains cannot take null, set default value to empty string
            sortBy = "name";

        //return userRepository.findAll(); //exposes all the data to clients (use DTOs)
        //use the stream api to map user objects to user dtos
        return userRepository.findAll(Sort.by(sortBy))//pass sort.by the sort field as an argument
                .stream()
                //map user to user dto and convert to list (if we change user entity, we just need to update mapping)
                //.map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())) //manual mapping user to userDto can become repetitive
                //instead better to use mapper to dto using MapStruct (user -> userMapper.toDto(user))
                .map(userMapper::toDto)
                .toList();//converts stream to list
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


    @PostMapping
    public ResponseEntity<UserDto> createUser(//response entity to modify response status
            @RequestBody RegisterUserRequest request,//use the RegisterUserRequest dto
            UriComponentsBuilder uriBuilder //uri builder for the location of the new resource created
    ) {
        var user = userMapper.toEntity(request); //map the request to an entity so we can work with it
        userRepository.save(user); //save user to db

        var userDto = userMapper.toDto(user); //map the user to a dto
        //build the uri location path with the path of the user and provide the id of the user as an argument
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);//return the userDto and status 201 (created)

        //if we don't care about Restful conventions (proper return status)
        //return userDto;     or
        //return ResponseEntity.ok(userDto);
    }
}
