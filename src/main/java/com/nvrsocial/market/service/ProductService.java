package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();
    public ProductService() {
        Product product1 = new Product(1, "Amnesia", "Mod for minecraft");
        Product product2 = new Product(2, "Amnesia 30d", "Mod for minecraft");

        products.add(product1);
        products.add(product2);

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
