package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.Product;
import com.nvrsocial.market.entity.ProductPlan;
import com.nvrsocial.market.entity.enums.ProductPeriod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();
    private List<ProductPlan> productPlans = new ArrayList<>();

    public ProductService() {
        Product product = new Product(
                1L, "Amnesia", "Mod for Minecraft", productPlans
        );

        ProductPlan weekPlan = new ProductPlan(
                1L, product, ProductPeriod.ONE_WEEK, new BigDecimal("4.99")
        );

        ProductPlan monthPlan = new ProductPlan(
                2L, product, ProductPeriod.ONE_MONTH, new BigDecimal("9.99")
        );

        ProductPlan threeMonthsPlan = new ProductPlan(
                3L, product, ProductPeriod.THREE_MONTHS, new BigDecimal("19.99")
        );

        products.add(product);

        productPlans.add(weekPlan);
        productPlans.add(monthPlan);
        productPlans.add(threeMonthsPlan);
    }

    public List<Product> getAllProduct(){
        return products;
    }

    public Product getProductForId(Long id){
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }
}
