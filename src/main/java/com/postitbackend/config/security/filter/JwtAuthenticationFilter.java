package com.postitbackend.config.security.filter;

import com.postitbackend.config.security.SecurityConstants;
import com.postitbackend.member.dto.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    {
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        authentication = authenticationManager.authenticate(authentication);

        log.info("인증 여부: " + authentication.isAuthenticated());

        if (!authentication.isAuthenticated()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return authentication;
    }

    /**
     * 인증 성공시
     * - JWT 토큰 생성
     * - JWT 토큰을 응답 헤더에 설정
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MemberDTO memberDTO = (MemberDTO) authResult.getPrincipal();
        Long id = memberDTO.getId();
        String email = memberDTO.getEmail();

        List<String> roles = memberDTO.getAuthList().stream()
                .map((auth) -> auth.getAuth())
                .collect(Collectors.toList());

        String jwt = jwtTokenProvider.createToken(id, email, roles);
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + jwt);
        response.setStatus(HttpStatus.OK.value());
    }

}