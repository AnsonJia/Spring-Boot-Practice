package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){//method to get the current user
        var authentication = SecurityContextHolder.getContext().getAuthentication();// returns auth object for the current user, which was set in the filter
        var userId = (Long) authentication.getPrincipal();//returns the current user/principal (id in our case) make sure to cast

        return userRepository.findById(userId).orElse(null);//return the user or null
    }
}
