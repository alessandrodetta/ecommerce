package com.cce.ecommerce.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne()
    @JsonIgnoreProperties("cart")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = { CascadeType.ALL })
    private List<CartItem> items = new ArrayList<>();

    private double totalAmount;

    public Cart(User user, List<CartItem> cartItems) {
        this.user = user;
        this.items = cartItems;
    }

    public Cart() {

    }

    public Cart(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isEmpty() {
        return items == null || items.size() <= 0;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double recalculateTotal() {
        this.totalAmount = 0;
        for (CartItem item : items) {
            item.recalculateTotal();
            this.totalAmount += item.getTotalAmount();
        }
        return this.totalAmount;
    }
}
