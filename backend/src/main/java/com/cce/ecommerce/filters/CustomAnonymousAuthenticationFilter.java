
package com.cce.ecommerce.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

// public class CustomAnonymousAuthenticationFilter extends
// AnonymousAuthenticationFilter {

// public CustomAnonymousAuthenticationFilter(String key, Object principal,
// List<GrantedAuthority> authorities) {
// super(key, principal, authorities);
// }

// @Override
// protected Authentication createAuthentication(HttpServletRequest request) {
// AnonymousAuthenticationToken token = new
// AnonymousAuthenticationToken(this.key, this.principal,
// this.authorities);
// token.setDetails(this.authenticationDetailsSource.buildDetails(request));
// return token;
// }
// }
