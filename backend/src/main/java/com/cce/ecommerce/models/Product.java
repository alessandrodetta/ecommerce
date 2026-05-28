package com.cce.ecommerce.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Fetch Mode viene ignorato nel caso di derived queries
    // Mentre nella navigazione viene applicato (es. anche con Jackson, o nel
    // codice)
    // Fetch type invece è considerato anche nelle derived queries
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JsonIgnoreProperties("products")
    private ProductCategory productCategory;

    private String title;
    private String description;
    private String detailedDescription;
    private Double price;
    private String image;

    public Product() {

    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Long getId() {
        return id;
    }
}
