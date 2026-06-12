package com.cce.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cce.ecommerce.models.Cart;
import com.cce.ecommerce.models.CartItem;
import com.cce.ecommerce.repositories.CartItemRepository;
import com.cce.ecommerce.repositories.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart addCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Transactional
    public Cart findByUserId(Long id) {
        return cartRepo.findByUserId(id);
    }

    public Cart updateCart(Cart cart) {
        return cartRepo.save(cart);
    }

    public void deleteItems(Cart cart) {
        // Copia degli items
        List<CartItem> itemsToRemove = new ArrayList<>(cart.getItems());
        // Sincronizzo
        for (CartItem cartItem : cart.getItems()) {
            cartItem.removeCart();
        }
        // Sincronizzo
        cart.getItems().clear();
        // Il manager elimina i figli (con la copia) perché non sono più collegati ad
        // alcun parent e quindi lo stato è coerente.
        cartItemRepository.deleteAll(itemsToRemove);
    }

    public Cart deleteItem(Cart cart, CartItem itemToRemove) {
        itemToRemove.removeCart();
        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);
        return cartRepo.save(cart);
    }

    public void deleteCart(Cart sourceCart) {
        sourceCart.getUser().setCart(null);
        cartRepo.delete(sourceCart);
    }
}
