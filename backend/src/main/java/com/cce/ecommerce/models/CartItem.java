package com.cce.ecommerce.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
/**
 * Vincolo di unicità composto necessario:
 * - Lo stesso carrello non può aggiungere due volte lo stesso prodotto
 * - Carrelli diversi però devono poter aggiungere lo stesso prodotto motivo per
 * cui product non è UNIQUE (by default/grazie a ManyToOne)
 */
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_cart_product", columnNames = { "cart_id", "product_id" })
})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JsonIgnoreProperties("items")
    private Cart cart;

    private int quantity;

    private double totalAmount;

    public double getTotalAmount() {
        return totalAmount;
    }

    // Empty constructor is necessary for reflection to work used by Hibernate
    public CartItem() {

    }

    public CartItem(Product product, Cart cart, int quantity) {
        this.product = product;
        this.cart = cart;
        this.quantity = quantity;
        this.totalAmount = quantity * product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Cart getCart() {
        return cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int update) {
        quantity += update;
        this.recalculateTotal();
    }

    public void removeCart() {
        this.cart = null;
    }

    public void recalculateTotal() {
        this.totalAmount = this.quantity * this.product.getPrice();
    }

}
