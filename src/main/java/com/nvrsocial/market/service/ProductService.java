package com.nvrsocial.market.service;

import com.nvrsocial.market.dto.request.ProductRequest;
import com.nvrsocial.market.dto.request.ProductPlanRequest;
import com.nvrsocial.market.dto.response.ProductResponse;
import com.nvrsocial.market.dto.response.ProductPlanResponse;
import com.nvrsocial.market.entity.enums.ProductPeriod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.nvrsocial.market.entity.Product;
import com.nvrsocial.market.entity.ProductPlan;
import com.nvrsocial.market.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> responses = new ArrayList<>();

        for (Product product : productRepository.findAll()) {
            List<ProductPlanResponse> plans = new ArrayList<>();
            for (ProductPlan plan : product.getProductPlans()) {
                plans.add(new ProductPlanResponse(plan.getId(), plan.getProductPeriod(), plan.getPrice()));
            }
            responses.add(new ProductResponse(product.getId(), product.getName(), product.getDescription(), plans));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return Optional.empty();
        }

        Product product = productOptional.get();
        List<ProductPlanResponse> plans = new ArrayList<>();
        for (ProductPlan plan : product.getProductPlans()) {
            plans.add(new ProductPlanResponse(plan.getId(), plan.getProductPeriod(), plan.getPrice()));
        }

        return Optional.of(new ProductResponse(product.getId(), product.getName(), product.getDescription(), plans));
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest newProduct) {
        Product product = new Product();
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());

        Set<ProductPeriod> periods = new HashSet<>();
        for (ProductPlanRequest requestedPlan : newProduct.getProductPlans()) {
            if (requestedPlan.getId() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New plan must not have an ID");
            }
            if (!periods.add(requestedPlan.getProductPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate product period");
            }

            ProductPlan plan = new ProductPlan();
            plan.setProduct(product);
            plan.setProductPeriod(requestedPlan.getProductPeriod());
            plan.setPrice(requestedPlan.getPrice());
            product.getProductPlans().add(plan);
        }

        Product savedProduct = productRepository.save(product);
        List<ProductPlanResponse> plans = new ArrayList<>();
        for (ProductPlan plan : savedProduct.getProductPlans()) {
            plans.add(new ProductPlanResponse(plan.getId(), plan.getProductPeriod(), plan.getPrice()));
        }

        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(), plans);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest product) {
        Product updateProduct = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        List<ProductPlan> oldPlans = new ArrayList<>(updateProduct.getProductPlans());
        List<ProductPlan> updatedPlans = new ArrayList<>();
        Set<ProductPeriod> periods = new HashSet<>();

        for (ProductPlanRequest requestedPlan : product.getProductPlans()) {
            if (!periods.add(requestedPlan.getProductPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate product period");
            }

            ProductPlan plan;
            if (requestedPlan.getId() == null) {
                plan = new ProductPlan();
            } else {
                plan = oldPlans.stream()
                        .filter(oldPlan -> oldPlan.getId().equals(requestedPlan.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan does not belong to product or ID is repeated"));

                Long count = entityManager.createQuery("select count(s) from Subscription s where s.productPlan.id = :id", Long.class).setParameter("id", plan.getId()).getSingleResult();

                if (count > 0 && plan.getProductPeriod() != requestedPlan.getProductPeriod()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot change period of a purchased plan");
                }

                oldPlans.remove(plan);
            }

            plan.setProduct(updateProduct);
            plan.setProductPeriod(requestedPlan.getProductPeriod());
            plan.setPrice(requestedPlan.getPrice());
            updatedPlans.add(plan);
        }

        for (ProductPlan oldPlan : oldPlans) {
            Long count = entityManager.createQuery("select count(s) from Subscription s where s.productPlan.id = :id", Long.class).setParameter("id", oldPlan.getId()).getSingleResult();

            if (count > 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete a purchased plan");
            }

            entityManager.remove(oldPlan);
        }

        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.getProductPlans().clear();
        updateProduct.getProductPlans().addAll(updatedPlans);

        for (ProductPlan plan : updatedPlans) {
            if (plan.getId() == null) {
                entityManager.persist(plan);
            }
        }

        Product savedProduct = productRepository.save(updateProduct);

        List<ProductPlanResponse> plans = new ArrayList<>();
        for (ProductPlan plan : savedProduct.getProductPlans()) {
            plans.add(new ProductPlanResponse(plan.getId(), plan.getProductPeriod(), plan.getPrice()));
        }
        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(), plans);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Long count = entityManager.createQuery("select count(s) from Subscription s where s.product.id = :id", Long.class).setParameter("id", id).getSingleResult();

        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product has subscriptions");
        }
        productRepository.delete(product);
    }
}
