package com.nvrsocial.market.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvrsocial.market.entity.enums.ProductPeriod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

@Entity
public class ProductPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    private ProductPeriod productPeriod;
    private BigDecimal price;

    public ProductPlan() {}

    public ProductPlan(Product product, ProductPeriod productPeriod, BigDecimal price) {
        this.product = product;
        this.productPeriod = productPeriod;
        this.price = price;
    }
}
