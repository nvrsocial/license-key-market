package com.nvrsocial.market.controller;

import com.nvrsocial.market.entity.Product;
import com.nvrsocial.market.service.ProductService;
import jakarta.persistence.Id;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<Product> allProducts(){
       return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
