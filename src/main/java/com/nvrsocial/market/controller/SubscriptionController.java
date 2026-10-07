package com.nvrsocial.market.controller;

import com.nvrsocial.market.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/buy/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void buy(@PathVariable Long planId, Principal principal) {
        subscriptionService.buy(principal.getName(), planId);
    }
}