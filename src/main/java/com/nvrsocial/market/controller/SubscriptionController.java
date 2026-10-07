package com.nvrsocial.market.controller;

import com.nvrsocial.market.service.SubscriptionService;
import com.nvrsocial.market.dto.request.PurchaseRequest;
import com.nvrsocial.market.dto.response.SubscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/buy")
    public SubscriptionResponse buy(@Valid @RequestBody PurchaseRequest purchase) {
        return subscriptionService.buy(purchase);
    }
}