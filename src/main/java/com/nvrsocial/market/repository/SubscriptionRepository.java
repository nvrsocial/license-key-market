package com.nvrsocial.market.repository;

import com.nvrsocial.market.entity.Subscription;
import com.nvrsocial.market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findSubscriptionByUserIdAndProductId(Long userId, Long productId);
    List<Subscription> getAllSubscriptionByUserId(Long userId);
}
