package com.cce.ecommerce.filters;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
// @Order(1)
public class GuestUserSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie guestUserId = null;
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (c.getName() == "guestUserId") {
                    guestUserId = c;
                    request.setAttribute("GUESTUSERID", Long.decode(c.getValue()));
                    break;
                }
            }
        }

        if (guestUserId == null) {
            guestUserId = new Cookie("GUESTUSERID", "0");
            guestUserId.setMaxAge(30 * 24 * 60 * 60);
            guestUserId.setPath("/");
            guestUserId.setHttpOnly(true);

            HttpServletResponse res = (HttpServletResponse) response;
            res.addCookie(guestUserId);
            request.setAttribute("GUESTUSERID", 0L);
        }

        chain.doFilter(request, response);
    }
}
