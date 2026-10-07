package com.nvrsocial.market.repository;

import com.nvrsocial.market.entity.ProductPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPlanRepository extends JpaRepository<ProductPlan, Long> {
}
