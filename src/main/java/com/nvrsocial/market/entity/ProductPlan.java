package com.nvrsocial.market.entity;

import com.nvrsocial.market.entity.enums.ProductPeriod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class ProductPlan {
    private long id;
    private Product product;
    private ProductPeriod productPeriod;
    private BigDecimal price;

    public ProductPlan(long id, Product product, ProductPeriod productPeriod, BigDecimal price) {
        this.id = id;
        this.product = product;
        this.productPeriod = productPeriod;
        this.price = price;
    }
}
