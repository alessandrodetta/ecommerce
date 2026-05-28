package com.cce.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cce.ecommerce.models.ProductCategory;
import com.cce.ecommerce.services.ProductCategoryService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/api/categories")
    public List<ProductCategory> getCategories() {
        return productCategoryService.getProductCategories();
    }
}
