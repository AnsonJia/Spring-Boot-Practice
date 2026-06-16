package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor//lombok constructor
@RequestMapping("/users") //mapping controller to the users endpoint to simplify url endpoint code
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;// add userMapper field
    // method: GET, POST, PUT, DELETE

    //get all users from database
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


    //creating a new user
    @PostMapping//not valid will throw a MethodArgumentNotValidException and needs to be handled, but not inside here because it won't get called
    public ResponseEntity<UserDto> createUser(//response entity to modify response status code
            //annotate with valid to validate the request body before the method gets called and return 400 bad request if invalid
            @Valid @RequestBody RegisterUserRequest request,//to read the request body using the RegisterUserRequest dto
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


    //update user info
    @PutMapping("/{id}") //put for updating/replacing resource, patch for patching/updating partial resources/properties (one field)
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id, //path variable for the user id we want to update
            @RequestBody UpdateUserRequest request //to read the request body using the UpdateUserRequest dto
    ) {
        var user = userRepository.findById(id).orElse(null);//check is the user exists
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        //we could set the fields manually or use the mapper to update the user entity with the request data
        //user.setName(request.getName());
        //user.setEmail(request.getEmail());

        userMapper.update(request, user); //map the request to the existing user entity (update user entity with the request data)
        userRepository.save(user); //save the updated user to the db

        return ResponseEntity.ok(userMapper.toDto(user));
    }


    //deleting a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { //void because we are not returning anything in response
        var user = userRepository.findById(id).orElse(null); //check if user exists
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build(); //status 204 no content because we don't have anything to return
    }


    //change user password
    @PostMapping("/{id}/change-password")//we use post because are creating a request to perform an action (validate old password and update)
    public ResponseEntity<Void> changePassword( //void because we are not returning anything in response
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request //read the request body
    ){
        var user = userRepository.findById(id).orElse(null); //check if user exists
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(request.getOldPassword())) { //validate the users old password matches with the request
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //no factory method for unauthorized so set manually
        }
        user.setPassword(request.getNewPassword());//if passwords match, update password field no mapper needed (only for larger objects)
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    //method for handling validation errors thrown by the @Valid annotation to provide meaningful error messages to client
    @ExceptionHandler(MethodArgumentNotValidException.class) //argument is the type of exception we want to handle
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException exception){
        //we want to return in the format "name": "Name is required" Map<String, String>
        var errors = new HashMap<String, String>();
        //loop through each error and add it to the map
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);//return the map of errors
    }
}
