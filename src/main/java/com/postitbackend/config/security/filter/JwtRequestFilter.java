package com.postitbackend.config.security.filter;

import com.postitbackend.config.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);

        // 요청 헤더에 jwt 토큰이 없는 경우, 다음 필터 진행
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");

        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

        if (jwtTokenProvider.validateToken(jwt)) {
            log.info("유효한 JWT 토큰");

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
