package com.nvrsocial.market.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotEmpty
    private List<@NotNull @Valid ProductPlanRequest> productPlans;

    public ProductRequest() {}

    public ProductRequest(String name, String description, List<@NotNull @Valid ProductPlanRequest> productPlans) {
        this.name = name;
        this.description = description;
        this.productPlans = productPlans;
    }
}
