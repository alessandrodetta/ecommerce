package com.cce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cce.ecommerce.resolvers.CurrentUser;
import com.cce.ecommerce.services.CartService;
import com.cce.ecommerce.services.ProductService;
import com.cce.ecommerce.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CartController {
    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @GetMapping("/api/cart")
    ResponseEntity<Cart> getCart(HttpServletResponse res, @CurrentUser User user) {
        // 1. Reperisco l'utente. User a null significa che è un visitatore nuovo.
        if (user == null) {
            return ResponseEntity.ok(new Cart());
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user, new ArrayList<CartItem>());
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/api/cart/items")
    ResponseEntity<Cart> addOrUpdateItem(HttpServletResponse response,
            @CurrentUser User user,
            @RequestBody() CartItemUpdateRequest updateRequest) {

        // 1. reperire l'utente o crearne uno nuovo
        if (user == null) {
            user = userService.createGuestUser();
            Cookie guestUserCookie = new Cookie("GUESTUSERID", user.getId().toString());
            guestUserCookie.setPath("/api");
            guestUserCookie.setMaxAge(30 * 24 * 60 * 60);
            guestUserCookie.setHttpOnly(true);
            response.addCookie(guestUserCookie);
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

        return ResponseEntity.ok(cart);
    }

}
