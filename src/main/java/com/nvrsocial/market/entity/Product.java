package com.nvrsocial.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<ProductPlan> productPlans = new ArrayList<>();
    public Product() {}

    public Product(String name, String description, List<ProductPlan> productPlans) {
        this.name = name;
        this.description = description;
        this.productPlans = productPlans;
    }
}
