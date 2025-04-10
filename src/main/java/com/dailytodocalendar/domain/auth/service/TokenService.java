package com.dailytodocalendar.domain.auth.service;

import com.dailytodocalendar.domain.auth.model.AuthToken;

/** 토큰 서비스 인터페이스 */
public interface TokenService {

  /**
   * 사용자 정보로 토큰 생성
   *
   * @param id 사용자 ID
   * @param email 사용자 이메일
   * @param role 사용자 역할
   * @return 생성된 인증 토큰
   */
  AuthToken createToken(Long id, String email, String role);

  /**
   * 토큰 유효성 검증
   *
   * @param token 검증할 토큰
   * @return 유효 여부
   */
  boolean validateToken(String token);

  /**
   * 토큰에서 이메일 추출
   *
   * @param token 토큰
   * @return 이메일
   */
  String getEmailFromToken(String token);
}
