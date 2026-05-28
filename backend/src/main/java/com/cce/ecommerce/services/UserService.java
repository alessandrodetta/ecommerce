package com.cce.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cce.ecommerce.models.Cart;
import com.cce.ecommerce.models.CartItem;
import com.cce.ecommerce.models.Product;
import com.cce.ecommerce.models.User;
import com.cce.ecommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    public User addUser(String username, String password, String role) {
        User user = new User(username, new BCryptPasswordEncoder().encode(password), role);
        userRepo.save(user);
        Cart cart = cartService.addCart(new Cart(user));
        user.setCart(cart);
        return user;
    }

    public User findById(Long id) {
        try {
            User user = userRepo.findById(id).get();
            return user;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public User createGuestUser() {
        User user = null;
        user = addUser(null, null, "ROLE_GUEST");
        return user;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public User registerGuest(String guestUserId, String username, String password) {
        User user = findById(Long.decode(guestUserId));
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole("ROLE_USER");
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void mergeCart(User dest, User source) {
        Cart sourceCart = source.getCart();
        Cart destCart = dest.getCart();
        if (!sourceCart.isEmpty() && destCart != null) {
            for (CartItem sourceItem : sourceCart.getItems()) {
                List<CartItem> destCartItems = destCart.getItems();
                Optional<CartItem> itemToUpdate = destCartItems.stream()
                        .filter(item -> item.getProduct().getId().equals(sourceItem.getProduct().getId())).findFirst();

                if (itemToUpdate.isPresent()) {
                    itemToUpdate.get().updateQuantity(sourceItem.getQuantity());
                } else {
                    Optional<Product> newProduct = productService.findById(sourceItem.getProduct().getId());
                    if (newProduct.isPresent()) {
                        destCartItems.add(new CartItem(newProduct.get(), destCart, sourceItem.getQuantity()));
                    }

                }
            }
            destCart.recalculateTotal();
            cartService.deleteItems(sourceCart);
        }
    }
}
