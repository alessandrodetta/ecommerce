package com.cce.ecommerce.models.dto;

import com.cce.ecommerce.models.ProductCategory;

public class ProductCategoryDTO {
    private String title;

    public ProductCategoryDTO(ProductCategory category) {
        this.title = category.getTitle();
    }

    public String getTitle() {
        return title;
    }
}
