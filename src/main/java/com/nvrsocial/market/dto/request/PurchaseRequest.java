package com.nvrsocial.market.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long planId;

    public PurchaseRequest() {}

    public PurchaseRequest(Long userId, Long planId) {
        this.userId = userId;
        this.planId = planId;
    }
}
