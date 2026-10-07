package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.User;
import com.nvrsocial.market.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User changeUser(Long id, User user) {
        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        updateUser.setUsername(user.getUsername());
        updateUser.setPassword(user.getPassword());
        updateUser.setEmail(user.getEmail());

        return userRepository.save(updateUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
