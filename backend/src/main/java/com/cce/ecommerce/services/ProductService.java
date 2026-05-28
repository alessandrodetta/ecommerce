package com.cce.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cce.ecommerce.models.Product;
import com.cce.ecommerce.models.ProductCategory;
import com.cce.ecommerce.repositories.ProductCategoryRepository;
import com.cce.ecommerce.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductCategoryRepository productCategProductRepository;

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepo.findById(id);
    }

    public List<Product> findByProductCategory(ProductCategory category) {
        return productRepo.findByProductCategory(category);
    }

    public List<ProductCategory> getProductCategories() {
        return productCategProductRepository.findAll();
    }
}
