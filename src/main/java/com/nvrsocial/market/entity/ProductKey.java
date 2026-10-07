package com.nvrsocial.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class ProductKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_key", nullable = false, unique = true)
    private String key;

    @OneToOne
    @JoinColumn(name = "subscription_id", nullable = false, unique = true)
    private Subscription subscription;

    public ProductKey() {}

    public ProductKey(String key, Subscription subscription) {
        this.key = key;
        this.subscription = subscription;
    }
}
