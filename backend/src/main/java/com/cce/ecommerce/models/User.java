package com.cce.ecommerce.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Username of the user
    @Column(unique = true)
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore()
    // Password of the user
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    // Authorities granted to the user, stored as a single string
    private String authorities;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    public User() {

    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.authorities = role;
    }

    public User(String role) {
        authorities = role;
    }

    public Long getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setRole(String role) {
        this.authorities = role;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
