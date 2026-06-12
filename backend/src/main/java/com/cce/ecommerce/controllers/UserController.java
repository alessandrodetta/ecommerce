package com.cce.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cce.ecommerce.models.Cart;
import com.cce.ecommerce.models.User;
import com.cce.ecommerce.models.requests.LoginRequest;
import com.cce.ecommerce.models.requests.RegisterRequest;
import com.cce.ecommerce.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    void login(HttpServletRequest request, HttpServletResponse response,
            @CookieValue(name = "GUESTUSERID", required = false) String guestUserId,
            @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken requestToken = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(requestToken);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        User authUser = ((User) authentication.getPrincipal());
        if (guestUserId != null && !authUser.getId().equals(Long.decode(guestUserId))) {
            User guestUser = userService.findById(Long.decode(guestUserId));
            Cart cart = guestUser.getCart();
            if (cart != null) {
                userService.mergeCart(authUser, guestUser);
            }
        }
    }

    @PostMapping("/api/register")
    void register(HttpServletResponse response, @CookieValue(name = "GUESTUSERID", required = false) String guestUserId,
            @RequestBody RegisterRequest registerRequest) {
        if (guestUserId != null) {
            userService.registerGuest(guestUserId, registerRequest.getUsername(),
                    registerRequest.getPassword());
            Cookie toBeDeletedCookie = new Cookie("GUESTUSERID", "");
            toBeDeletedCookie.setMaxAge(0);
            response.addCookie(toBeDeletedCookie);
        } else {
            userService.addUser(registerRequest.getUsername(),
                    registerRequest.getPassword(), "ROLE_USER");
        }
    }
}
