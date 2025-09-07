package com.SpringPractice.store.UserRegistrationService;


import com.SpringPractice.store.NotificationService.NotificationService;
import com.SpringPractice.store.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // register user function
    public void registerUser(User user){
        // if user email exists, throw error
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new IllegalArgumentException("User with email "+user.getEmail() + " already exists");
        }
        // if not exist, save user and send message
        userRepository.save(user);
        notificationService.send("Registration Successful", user.getEmail());
    }
}
