package com.cce.ecommerce.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.cce.ecommerce.models.User;
import com.cce.ecommerce.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CurrentUserArgumentResolver
    implements HandlerMethodArgumentResolver {

  @Autowired
  private UserService userService;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterAnnotation(CurrentUser.class) != null;
  }

  @Override
  public Object resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory) throws Exception {

    HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
    String guestUserId = null;
    if (request.getCookies() != null) {
      for (Cookie c : request.getCookies()) {
        if ("GUESTUSERID".equals(c.getName())) {
          guestUserId = c.getValue();
          break;
        }
      }
    }
    // È corretto questo modo di ottenere l'autenticazione? Prima usavo
    // @CurrentSecurityContext
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user;
    if (auth instanceof AnonymousAuthenticationToken) {
      if (guestUserId == null) {
        return null;
      }
      user = userService.findById(Long.decode(guestUserId));
    } else {
      user = (User) auth.getPrincipal();
    }
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    return user;
  }
}
