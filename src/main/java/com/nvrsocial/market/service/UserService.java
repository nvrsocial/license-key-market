package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.User;
import com.nvrsocial.market.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
