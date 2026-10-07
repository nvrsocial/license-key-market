package com.nvrsocial.market.service;

import com.nvrsocial.market.dto.request.PurchaseRequest;
import com.nvrsocial.market.dto.response.SubscriptionResponse;
import com.nvrsocial.market.dto.response.ProductPlanResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.LockModeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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

    @PersistenceContext
    private EntityManager entityManager;

    public SubscriptionService(UserRepository userRepository, ProductPlanRepository productPlanRepository,
                               SubscriptionRepository subscriptionRepository, ProductKeyRepository productKeyRepository, KeyGenerator keyGenerator) {
        this.userRepository = userRepository;
        this.productPlanRepository = productPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.productKeyRepository = productKeyRepository;
        this.keyGenerator = keyGenerator;
    }

    @Transactional
    public SubscriptionResponse buy(PurchaseRequest purchase) {
        User user = userRepository.findById(purchase.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE);
        ProductPlan productPlan = productPlanRepository.findById(purchase.getPlanId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductPlan not found"));

        Product product = productPlan.getProduct();

        Subscription subscription = subscriptionRepository.findSubscriptionByUserIdAndProductId(user.getId(), product.getId()).orElse(null);

        LocalDateTime now = LocalDateTime.now();

        if (subscription != null && subscription.getExpireAt().isAfter(now)) {
            int currentLvl = subscription.getProductPlan().getProductPeriod().getLvl();
            int newLvl = productPlan.getProductPeriod().getLvl();

            if (newLvl <= currentLvl) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot purchase the same or a lower active plan");
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

        ProductPlanResponse planResponse = new ProductPlanResponse(productPlan.getId(), productPlan.getProductPeriod(), productPlan.getPrice());
        return new SubscriptionResponse(savedSubscription.getId(), user.getId(), product.getId(),
                product.getName(), planResponse, savedSubscription.getStartAt(), savedSubscription.getExpireAt(),
                savedSubscription.getExpireAt().isAfter(LocalDateTime.now()), productKey.getKey());
    }
}
