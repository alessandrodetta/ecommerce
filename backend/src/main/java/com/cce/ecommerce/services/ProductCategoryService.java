package com.cce.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cce.ecommerce.models.ProductCategory;
import com.cce.ecommerce.repositories.ProductCategoryRepository;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategProductRepository;

    public List<ProductCategory> getProductCategories() {
        return productCategProductRepository.findAll();
    }
    /**
     * For the future, in case of DTO of ProductCategory
     * public List<ProductCategoryDTO> getProductCategories() {
     * return
     * productCategProductRepository.findAll().stream().map(ProductCategoryDTO::new)
     * .collect(Collectors.toList());
     * }
     */

    // public List<Product> getProductsByCategoryId(Long id) {
    // Optional<ProductCategory> cat = productCategProductRepository.findById(id);
    // if(cat.isPresent()) {
    // return cat.
    // }
    // return ;
    // }
}
