package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth") //mapping controller to the auth endpoint to simplify url endpoint code
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login") //mapping controller to the login endpoint to simplify url endpoint code
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request){
        //using DaoAuthentication to delegate authentication to the manager and replace the code below
        authenticationManager.authenticate(//if bad credentials, it will throw a BadCredentialsException which needs to be handled
                new UsernamePasswordAuthenticationToken( //need to provide an authentication object
                        request.getEmail(),
                        request.getPassword()
                ));
        //AuthController is still handling business logic and should be moved elsewhere but not need for a service since it's already implemented in spring security
        /*var user = userRepository.findByEmail(request.getEmail()).orElse(null);//check if email maps to an existing user
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){//check if passwords match
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        //return ResponseEntity.ok().build();

        var token = jwtService.generateToken(request.getEmail());//generate the token using the users email

        return ResponseEntity.ok(new JwtResponse(token));//newing an object requires constructors so set all args in JwtResponse
    }

    @ExceptionHandler(BadCredentialsException.class)//exception handler to check for bad login credentials
    public ResponseEntity<Void> handleBadCredentialsExceptions(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();//returns a 401 unauthorized
    }
}
