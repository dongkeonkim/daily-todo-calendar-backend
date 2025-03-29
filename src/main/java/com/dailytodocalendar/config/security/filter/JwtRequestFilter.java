package com.dailytodocalendar.config.security.filter;

import com.dailytodocalendar.config.security.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  /** 요청 헤더에서 올바른 JWT 를 사용하고 있는지 확인 */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String header = request.getHeader(SecurityConstants.TOKEN_HEADER);

      if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
        filterChain.doFilter(request, response);
        return;
      }

      String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");

      if (jwtTokenProvider.validateToken(jwt)) {
        UsernamePasswordAuthenticationToken authentication =
            jwtTokenProvider.getAuthentication(jwt);

        if (authentication != null) {
          SecurityContextHolder.getContext().setAuthentication(authentication);
          log.debug("Authentication successful for user: {}", authentication.getPrincipal());
        }
      } else {
        log.warn("Invalid JWT token in request");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    } catch (Exception e) {
      log.error("Authentication error: {}", e.getMessage());
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Authentication failed: " + e.getMessage());
      return;
    }

    filterChain.doFilter(request, response);
  }
}
