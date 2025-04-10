package com.dailytodocalendar.domain.member.service;

/** 비밀번호 검증 서비스 인터페이스 - 도메인 서비스로, 특정 기술에 의존하지 않는 인터페이스 */
public interface PasswordVerificationService {

  /**
   * 비밀번호 검증
   *
   * @param rawPassword 원본(평문) 비밀번호
   * @param encodedPassword 인코딩된 비밀번호
   * @return 일치 여부
   */
  boolean verify(String rawPassword, String encodedPassword);

  /**
   * 비밀번호 인코딩
   *
   * @param rawPassword 원본(평문) 비밀번호
   * @return 인코딩된 비밀번호
   */
  String encode(String rawPassword);
}
