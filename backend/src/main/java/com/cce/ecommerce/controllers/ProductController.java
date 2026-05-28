package com.cce.ecommerce.controllers;

import com.cce.ecommerce.models.Product;
import com.cce.ecommerce.models.ProductCategory;
import com.cce.ecommerce.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/api/products")
    public List<Product> getProducts(@RequestParam(required = false) ProductCategory category) {
        if (category != null) {
            return productService.findByProductCategory(category);
        }
        List<Product> products = productService.getProducts();
        return products;
    }
}
