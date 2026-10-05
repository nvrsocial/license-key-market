package com.nvrsocial.market.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Product {
    private long id;
    private String name;
    private String description;

    public Product(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
