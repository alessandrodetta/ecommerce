package com.cce.ecommerce.repositories;

import com.cce.ecommerce.models.Product;
import com.cce.ecommerce.models.ProductCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @Query("select p from Product p join fetch p.productCategory")
    List<Product> findAll();

    List<Product> findByProductCategory(ProductCategory category);
}
