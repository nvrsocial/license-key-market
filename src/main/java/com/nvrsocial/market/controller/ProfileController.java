package com.nvrsocial.market.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class ProfileController {

    @GetMapping("/api/me")
    public Map<String, String> me(Principal principal) {
        return Map.of("username", principal.getName());
    }
}