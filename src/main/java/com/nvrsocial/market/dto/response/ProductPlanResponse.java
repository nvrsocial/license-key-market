package com.nvrsocial.market.dto.response;

import com.nvrsocial.market.entity.enums.ProductPeriod;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPlanResponse {

    private Long id;

    private ProductPeriod productPeriod;

    private BigDecimal price;

    public ProductPlanResponse() {
    }

    public ProductPlanResponse(Long id, ProductPeriod productPeriod, BigDecimal price) {
        this.id = id;
        this.productPeriod = productPeriod;
        this.price = price;
    }
}
