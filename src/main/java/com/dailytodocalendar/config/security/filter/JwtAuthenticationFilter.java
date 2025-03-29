package com.dailytodocalendar.config.security.filter;

import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.config.security.constants.SecurityConstants;
import com.dailytodocalendar.config.security.custom.CustomUser;
import jakarta.servlet.FilterChain;
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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  /** 사용자 인증 여부 확인 */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    String username = request.getParameter("email");
    String password = request.getParameter("password");

    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

    try {
      authentication = authenticationManager.authenticate(authentication);
      log.info("인증 여부: " + authentication.isAuthenticated());
    } catch (AuthenticationException e) {
      log.warn(e.getMessage());
    }

    return authentication;
  }

  /** 인증 성공시 - JWT 토큰 생성 - JWT 토큰을 응답 헤더에 설정 */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    CustomUser customUser = (CustomUser) authResult.getPrincipal();
    MemberDto memberDto = customUser.getMemberDto();

    Long id = memberDto.getId();
    String email = memberDto.getEmail();
    String roles = memberDto.getRole();

    String jwt = jwtTokenProvider.createToken(id, email, roles);
    response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + jwt);
    response.setStatus(HttpStatus.OK.value());
  }
}
