package com.dailytodocalendar.adapter.in.web.common;

import com.dailytodocalendar.infrastructure.config.security.custom.CustomUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** 현재 로그인한 사용자를 주입하는 ArgumentResolver - @CurrentUser 애너테이션이 붙은 파라미터에 현재 사용자 정보 주입 */
@Component
@Slf4j
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    // CurrentUser 애너테이션이 붙은 파라미터만 처리
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Object resolveArgument(
      @NonNull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      log.debug("인증 정보가 없습니다.");
      return null;
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof CustomUser) {
      log.debug("현재 사용자 정보 주입: {}", ((CustomUser) principal).getUsername());
      return principal;
    }

    log.debug("인증 객체가 CustomUser 타입이 아닙니다: {}", principal.getClass().getName());
    return null;
  }
}
