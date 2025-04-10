package com.dailytodocalendar.infrastructure.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/** JWT 인증 필터 - 모든 요청에 대해 JWT 토큰을 검증하고 SecurityContext에 인증 정보를 설정 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  // 인증이 필요없는 경로
  private static final List<String> EXCLUDE_PATHS =
      Arrays.asList(
          "/",
          "/auth/**",
          "/api-docs/**",
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/actuator/**",
          "/h2-console/**",
          "/error");

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      // 요청에서 JWT 토큰 추출
      String token = extractToken(request);

      // 토큰이 유효하면 인증 정보 설정
      if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.debug("인증 정보 설정 완료: {}", auth.getName());
      }
    } catch (Exception e) {
      log.error("JWT 인증 필터 처리 중 오류 발생: {}", e.getMessage());
      // JwtExceptionFilter에서 예외 처리를 위해 SecurityContext를 비움
      SecurityContextHolder.clearContext();
      // 예외를 다시 던지지 않고 필터 체인 계속 진행
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return EXCLUDE_PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));
  }

  /** 요청 헤더에서 JWT 토큰 추출 */
  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
