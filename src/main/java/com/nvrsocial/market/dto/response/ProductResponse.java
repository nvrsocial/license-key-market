package com.nvrsocial.market.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private List<ProductPlanResponse> productPlans;

    public ProductResponse() {}

    public ProductResponse(Long id, String name, String description, List<ProductPlanResponse> productPlans) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productPlans = productPlans;
    }
}
