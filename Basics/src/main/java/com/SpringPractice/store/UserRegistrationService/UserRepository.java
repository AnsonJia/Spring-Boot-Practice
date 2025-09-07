package com.SpringPractice.store.UserRegistrationService;

import com.SpringPractice.store.User;

// user registration methods to be implemented
public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}
