package com.SpringPractice.store.UserRegistrationService;

import com.SpringPractice.store.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

// pretend repository access
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        System.out.println("Saving user: " + user);
        users.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        return users.getOrDefault(email, null);
    }
}
