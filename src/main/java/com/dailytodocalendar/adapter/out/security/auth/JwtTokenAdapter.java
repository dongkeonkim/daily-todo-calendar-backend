package com.dailytodocalendar.adapter.out.security.auth;

import com.dailytodocalendar.domain.auth.model.AuthToken;
import com.dailytodocalendar.domain.auth.service.TokenService;
import com.dailytodocalendar.infrastructure.config.security.filter.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** JWT 토큰 어댑터 - 아웃바운드 어댑터 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenService {

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 사용자 정보로 토큰 생성
   *
   * @param id 사용자 ID
   * @param email 사용자 이메일
   * @param role 사용자 역할
   * @return 생성된 인증 토큰
   */
  @Override
  public AuthToken createToken(Long id, String email, String role) {
    String token = jwtTokenProvider.createToken(id, email, role);
    long expirationInMs = jwtTokenProvider.getTokenExpirationInMs();
    return AuthToken.of(token, expirationInMs);
  }

  /**
   * 토큰 유효성 검증
   *
   * @param token 검증할 토큰
   * @return 유효 여부
   */
  @Override
  public boolean validateToken(String token) {
    return jwtTokenProvider.validateToken(token);
  }

  /**
   * 토큰에서 이메일 추출
   *
   * @param token 토큰
   * @return 이메일
   */
  @Override
  public String getEmailFromToken(String token) {
    return jwtTokenProvider.getEmailFromToken(token);
  }
}
