package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.User;
import com.nvrsocial.market.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("Username is empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is empty");
        }

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        userRepository.save(newUser);

        return newUser;
    }

    public User login(String username, String password) {
        if (username == null ||username.isEmpty()) {
            throw new RuntimeException("Username is empty");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password is empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not exists"));

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new RuntimeException("Password incorrect");
        }
    }
}
