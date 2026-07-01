package com.codewithmosh.store.services;

import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service//mark as bean so spring can create instances at runtime
public class UserService implements UserDetailsService {//implementation of UserDetailsService for DaoAuthenticationM
    private final UserRepository userRepository;

    @Override//uds is an abstraction for finding a user just like a repository and contains 1 method to find the user
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(//we find the user by their unique email instead of username
                () -> new UsernameNotFoundException("User not found"));//throw exception (behavior spring expects from UDS) to be handled by the controller

        //if user is found, return a UserDetails object and spring security has an implementation of it called User
        return new User(//User defined by org.springframework.security, not the user entity we defined
                //User needs 3 fields, username (email in this case), password and authorities (permissions)
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()//we don't care about authorities right now
        );
    }
}
