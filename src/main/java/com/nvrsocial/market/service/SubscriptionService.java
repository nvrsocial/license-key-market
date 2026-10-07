package com.nvrsocial.market.service;

import com.nvrsocial.market.component.KeyGenerator;
import com.nvrsocial.market.entity.*;
import com.nvrsocial.market.repository.ProductKeyRepository;
import com.nvrsocial.market.repository.ProductPlanRepository;
import com.nvrsocial.market.repository.SubscriptionRepository;
import com.nvrsocial.market.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionService {
    private final UserRepository userRepository;
    private final ProductPlanRepository productPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ProductKeyRepository productKeyRepository;

    private final KeyGenerator keyGenerator;

    public SubscriptionService(UserRepository userRepository, ProductPlanRepository productPlanRepository,
                               SubscriptionRepository subscriptionRepository, ProductKeyRepository productKeyRepository, KeyGenerator keyGenerator) {
        this.userRepository = userRepository;
        this.productPlanRepository = productPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.productKeyRepository = productKeyRepository;
        this.keyGenerator = keyGenerator;
    }

    @Transactional
    public void buy(String username, Long planId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ProductPlan productPlan = productPlanRepository.findById(planId).orElseThrow(() -> new RuntimeException("ProductPlan not found"));

        Product product = productPlan.getProduct();

        Subscription subscription = subscriptionRepository.findSubscriptionByUserIdAndProductId(user.getId(), product.getId()).orElse(null);

        LocalDateTime now = LocalDateTime.now();

        if (subscription != null && subscription.getExpireAt().isAfter(now)) {
            int currentLvl = subscription.getProductPlan().getProductPeriod().getLvl();
            int newLvl = productPlan.getProductPeriod().getLvl();

            if (newLvl <= currentLvl) {
                throw new IllegalStateException("You cannot purchase the same or a lower active plan");
            }
        }

        LocalDateTime expireAt = switch (productPlan.getProductPeriod()) {
            case ONE_WEEK -> now.plusWeeks(1);
            case ONE_MONTH -> now.plusMonths(1);
            case THREE_MONTHS -> now.plusMonths(3);
        };

        if (subscription == null) {
            subscription = new Subscription();
        }

        subscription.setUser(user);
        subscription.setProduct(product);
        subscription.setProductPlan(productPlan);
        subscription.setStartAt(now);
        subscription.setExpireAt(expireAt);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        String generatedKey = keyGenerator.keyGeneration();

        ProductKey productKey = productKeyRepository.getProductKeyBySubscriptionId(savedSubscription.getId()).orElse(null);

        if (productKey == null) {
            productKey = new ProductKey(generatedKey, savedSubscription);
        } else {
            productKey.setKey(generatedKey);
        }

        productKeyRepository.save(productKey);
    }
}
