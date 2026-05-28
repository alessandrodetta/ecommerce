package com.cce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cce.ecommerce.models.Cart;
import com.cce.ecommerce.models.CartItem;
import com.cce.ecommerce.models.Product;
import com.cce.ecommerce.models.User;
import com.cce.ecommerce.models.requests.CartItemUpdateRequest;
import com.cce.ecommerce.services.CartService;
import com.cce.ecommerce.services.ProductService;
import com.cce.ecommerce.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class CartController {
    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    // @GetMapping("/api/user")
    // User getUser(@RequestParam Long id) {
    // return userService.findById(id);
    // }

    @GetMapping("/api/cart")
    Cart getCart(HttpServletRequest request, @CookieValue(name = "GUESTUSERID", required = false) String guestUserId,
            @CurrentSecurityContext() SecurityContext securityContext)
            throws Exception {
        Authentication auth = securityContext.getAuthentication();
        Long userId = null;
        if (auth instanceof AnonymousAuthenticationToken) {
            if (guestUserId == null) {
                return new Cart();
            }
            userId = Long.decode(guestUserId);
        } else {
            User user = (User) auth.getPrincipal();
            if (user == null) {
                throw new Exception("Session with no authentication");
            }
            userId = user.getId();
        }

        User user = userService.findById(userId);
        Cart cart = user.getCart();
        return cart;
    }

    @PostMapping("/api/cart/items")
    Cart addOrUpdateItem(HttpServletRequest request, HttpServletResponse response,
            @CookieValue(name = "GUESTUSERID", required = false) String guestUserId,
            @RequestBody() CartItemUpdateRequest updateRequest,
            @CurrentSecurityContext() SecurityContext securityContext)
            throws Exception {

        Authentication auth = securityContext.getAuthentication();

        // 1. reperire l'utente o crearne uno nuovo
        User user = null;
        if (auth instanceof AnonymousAuthenticationToken) {
            if (guestUserId == null) {
                user = userService.createGuestUser();
                Cookie guestUserCookie = new Cookie("GUESTUSERID", user.getId().toString());
                guestUserCookie.setPath("/api");
                guestUserCookie.setMaxAge(30 * 24 * 60 * 60);
                guestUserCookie.setHttpOnly(true);
                response.addCookie(guestUserCookie);
            } else {
                user = userService.findById(Long.decode(guestUserId));
            }
        } else {
            user = (User) auth.getPrincipal();
            if (user == null) {
                throw new Exception("Session with no authentication");
            }
            user = userService.findById(user.getId());
        }

        // 2. reperire il carrello dell'utente o ne crea uno nuovo
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user, new ArrayList<CartItem>());
        }

        // 3.aggiunge l'item al carrello
        List<CartItem> cartItems = cart.getItems();
        Optional<CartItem> itemToUpdate = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(updateRequest.getProductId())).findFirst();

        if (itemToUpdate.isPresent()) {
            itemToUpdate.get().updateQuantity(updateRequest.getQuantity());
            if (itemToUpdate.get().getQuantity() <= 0) {
                cartService.deleteItem(cart, itemToUpdate.get());
            }
        } else {
            Optional<Product> newProduct = productService.findById(updateRequest.getProductId());
            if (newProduct.isPresent()) {
                cartItems.add(new CartItem(newProduct.get(), cart, updateRequest.getQuantity()));
            }
        }

        cart.recalculateTotal();

        cart = cartService.updateCart(cart);

        return cart;
    }

    // @DeleteMapping("/api/cart/items")
    // Cart removeItem(@CookieValue(name = "GUESTUSERID", required = false) String
    // guestUserId,
    // @RequestBody() CartItem itemToRemove, @CurrentSecurityContext()
    // SecurityContext securityContext)
    // throws Exception {
    // Authentication auth = securityContext.getAuthentication();
    // User user = null;
    // if (auth instanceof AnonymousAuthenticationToken) {
    // if (guestUserId == null) {
    // return null;
    // } else {
    // user = userService.findById(Long.decode(guestUserId));
    // }
    // } else {
    // user = (User) auth.getPrincipal();
    // if (user == null) {
    // throw new Exception("Session with no authentication");
    // }
    // user = userService.findById(user.getId());
    // }

    // Cart cart = user.getCart();

    // List<CartItem> cartItems = cart.getItems();
    // Optional<CartItem> itemToUpdate = cartItems.stream()
    // .filter(item ->
    // item.getProduct().getId().equals(itemToRemove.getProduct().getId())).findFirst();

    // if (itemToUpdate.isPresent()) {
    // itemToUpdate.get().decrementQuantity(itemToRemove.getQuantity());
    // if (itemToUpdate.get().getQuantity() <= 0) {
    // cartService.deleteItem(cart, itemToUpdate.get());
    // }
    // }

    // cart = cartService.updateCart(cart);

    // return cart;
    // }

}
