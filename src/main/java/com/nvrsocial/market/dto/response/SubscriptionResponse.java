package com.nvrsocial.market.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionResponse {

    private Long id;

    private Long userId;

    private Long productId;

    private String productName;

    private ProductPlanResponse productPlan;

    private LocalDateTime startAt;

    private LocalDateTime expireAt;

    private boolean active;

    private String licenseKey;

    public SubscriptionResponse() {}

    public SubscriptionResponse(Long id, Long userId, Long productId, String productName,
            ProductPlanResponse productPlan, LocalDateTime startAt, LocalDateTime expireAt, boolean active, String licenseKey) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productPlan = productPlan;
        this.startAt = startAt;
        this.expireAt = expireAt;
        this.active = active;
        this.licenseKey = licenseKey;
    }
}
