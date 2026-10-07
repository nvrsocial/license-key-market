package com.nvrsocial.market.repository;

import com.nvrsocial.market.entity.ProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductKeyRepository extends JpaRepository<ProductKey, Long> {
    Optional<ProductKey> getProductKeyBySubscriptionId(Long id);
}
