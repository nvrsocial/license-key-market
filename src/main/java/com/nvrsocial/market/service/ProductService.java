package com.nvrsocial.market.service;

import com.nvrsocial.market.entity.Product;
import com.nvrsocial.market.entity.ProductPlan;
import com.nvrsocial.market.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product newProduct) {
        Product product = new Product();

        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());

        if (newProduct.getProductPlans() != null) {
            for (ProductPlan plan : newProduct.getProductPlans()) {
                plan.setProduct(product);
                product.getProductPlans().add(plan);
            }
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product updateProduct = productRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Product not found"));

        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setProductPlans(product.getProductPlans());

        return productRepository.save(updateProduct);

    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
