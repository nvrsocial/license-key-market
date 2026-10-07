package com.nvrsocial.market.dto.request;

import jakarta.validation.constraints.*;
import com.nvrsocial.market.entity.enums.ProductPeriod;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPlanRequest {

    private Long id;

    @NotNull
    private ProductPeriod productPeriod;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    public ProductPlanRequest() {}

    public ProductPlanRequest(Long id, ProductPeriod productPeriod, BigDecimal price) {
        this.id = id;
        this.productPeriod = productPeriod;
        this.price = price;
    }
}
