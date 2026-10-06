package com.nvrsocial.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Product {
    private long id;
    private String name;
    private String description;

    private List<ProductPlan> productPlans;

    public Product(long id, String name, String description, List<ProductPlan> productPlans) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productPlans = productPlans;
    }
}
