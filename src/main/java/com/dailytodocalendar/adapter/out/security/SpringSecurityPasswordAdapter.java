package com.dailytodocalendar.adapter.out.security;

import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import com.dailytodocalendar.domain.member.service.PasswordVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** 스프링 시큐리티 기반 비밀번호 어댑터 - 도메인 서비스 인터페이스를 스프링 시큐리티로 구현 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpringSecurityPasswordAdapter implements PasswordVerificationService {

  private final PasswordEncoder passwordEncoder;

  /**
   * 비밀번호 검증
   *
   * @param rawPassword 원본 비밀번호
   * @param encodedPassword 인코딩된 비밀번호
   * @return 일치 여부
   * @throws MemberDomainException 파라미터가 유효하지 않을 때
   */
  @Override
  public boolean verify(String rawPassword, String encodedPassword) {
    if (rawPassword == null || rawPassword.isEmpty()) {
      throw new MemberDomainException("원본 비밀번호는 필수 입력값입니다.");
    }

    if (encodedPassword == null || encodedPassword.isEmpty()) {
      throw new MemberDomainException("인코딩된 비밀번호는 필수 입력값입니다.");
    }

    try {
      return passwordEncoder.matches(rawPassword, encodedPassword);
    } catch (Exception e) {
      log.error("비밀번호 검증 중 오류 발생", e);
      throw new MemberDomainException("비밀번호 검증 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 비밀번호 인코딩
   *
   * @param rawPassword 원본 비밀번호
   * @return 인코딩된 비밀번호
   * @throws MemberDomainException 파라미터가 유효하지 않거나 인코딩 중 오류 발생 시
   */
  @Override
  public String encode(String rawPassword) {
    if (rawPassword == null || rawPassword.isEmpty()) {
      throw new MemberDomainException("원본 비밀번호는 필수 입력값입니다.");
    }

    try {
      return passwordEncoder.encode(rawPassword);
    } catch (Exception e) {
      log.error("비밀번호 인코딩 중 오류 발생", e);
      throw new MemberDomainException("비밀번호 인코딩 중 오류가 발생했습니다.", e);
    }
  }
}
