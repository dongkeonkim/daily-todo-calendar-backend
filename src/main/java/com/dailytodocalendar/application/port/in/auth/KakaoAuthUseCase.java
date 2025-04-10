package com.dailytodocalendar.application.port.in.auth;

import com.dailytodocalendar.domain.auth.model.AuthToken;

/** 카카오 인증 유스케이스 인터페이스 */
public interface KakaoAuthUseCase {

  /**
   * 카카오 로그인 처리
   *
   * @param code 카카오 인증 코드
   * @return 인증 토큰
   */
  AuthToken processKakaoLogin(String code);

  /** 카카오 계정 연결 해제 및 회원 탈퇴 */
  void unlinkKakaoAccount();
}
