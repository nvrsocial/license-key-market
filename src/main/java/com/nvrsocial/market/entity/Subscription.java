package com.nvrsocial.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "subscriptions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_plan_id", nullable = false)
    private ProductPlan productPlan;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    public Subscription() {}

    public Subscription(User user, Product product, ProductPlan productPlan, LocalDateTime startAt, LocalDateTime expireAt) {
        this.user = user;
        this.product = product;
        this.productPlan = productPlan;
        this.startAt = startAt;
        this.expireAt = expireAt;
    }
}
