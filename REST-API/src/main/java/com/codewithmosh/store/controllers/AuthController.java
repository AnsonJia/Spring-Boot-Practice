package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserMapper userMapper;

    @PostMapping("/login") //mapping controller to the login endpoint to simplify url endpoint code
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response){//provides low level access to http response for our cookie
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

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();//find user by email (throw exception to be handled by auth manager)
        var accessToken = jwtService.generateAccessToken(user);//pass whole user object (so we can add username and email as claims)
        var refreshToken = jwtService.generateRefreshToken(user);//generate refresh token and store it

        //we put refresh token into http only cookies which are not accessible by JavaScript making it hard to steal
        var cookie = new Cookie("refreshToken", refreshToken);//Cookie defined in jakarta.servlet.http (name, value)
        cookie.setHttpOnly(true);//set http only so not accessible by JavaScript
        cookie.setPath("/auth/refresh");//specifies where the cookie can be sent to
        cookie.setMaxAge(604800);//expiration (same value as token expiration in JwtService)
        cookie.setSecure(true);//only will be sent over http connections (prevent being exposed on unencrypted http channels)
        response.addCookie(cookie);//put cookie in the response

        return ResponseEntity.ok(new JwtResponse(accessToken));//newing an object requires constructors so set all args in JwtResponse
    }

    //temporary solution to validate token, normally we would use a filter to validate the token for every request, not  a separate endpoint
    @PostMapping("/validate")//endpoint is protected by default so configure in security config (removed it to test filter method)
    public boolean validate(@RequestHeader("Authorization") String authHeader){//authorization header is standard for passing credentials
        //System.out.println("Validate called");
        //when sending token in auth header, prefix with Bearer, but that would invalidate the token, so replace it before sending to service
        var token = authHeader.replace("Bearer ", "");//not extracting token inside service because it expects token, not auth header
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")//endpoint to get the current user
    public ResponseEntity<UserDto> me(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();// returns auth object for the current user, which was set in the filter
        var userId = (Long) authentication.getPrincipal();//returns the current user/principal (id in our case) make sure to cast

        var user = userRepository.findById(userId).orElse(null);//check if user exists
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        var userDto = userMapper.toDto(user);//map to dto and return it
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(BadCredentialsException.class)//exception handler to check for bad login credentials
    public ResponseEntity<Void> handleBadCredentialsExceptions(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();//returns a 401 unauthorized
    }
}
