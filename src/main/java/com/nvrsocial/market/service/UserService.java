package com.nvrsocial.market.service;

import com.nvrsocial.market.dto.request.UserUpdateRequest;
import com.nvrsocial.market.dto.response.UserResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import com.nvrsocial.market.entity.User;
import com.nvrsocial.market.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail())).toList();
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Transactional
    public UserResponse changeUser(Long id, UserUpdateRequest user) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!updateUser.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());

        try {
            userRepository.saveAndFlush(updateUser);
        } catch (org.springframework.dao.DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User data conflicts with an existing account");
        }
        return new UserResponse(updateUser.getId(), updateUser.getUsername(), updateUser.getEmail());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Long count = entityManager.createQuery("select count(s) from Subscription s where s.user.id = :id", Long.class).setParameter("id", id).getSingleResult();

        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has subscriptions");
        }
        userRepository.delete(user);
    }

}
