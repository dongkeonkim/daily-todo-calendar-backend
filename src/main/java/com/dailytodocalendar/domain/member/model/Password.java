package com.dailytodocalendar.domain.member.model;

import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** 비밀번호 값 객체 */
@Getter
@EqualsAndHashCode
public class Password {
  private final String value;

  private Password(String value) {
    this.value = value;
  }

  /**
   * 비밀번호 생성 팩토리 메서드 (인코딩된 비밀번호 저장용)
   *
   * @param encodedPassword 인코딩된 비밀번호
   * @return 비밀번호 값 객체
   * @throws MemberDomainException 비밀번호가 유효하지 않을 때
   */
  public static Password of(String encodedPassword) {
    validate(encodedPassword);
    return new Password(encodedPassword);
  }

  /**
   * 비밀번호 유효성 검사
   *
   * @param value 비밀번호 문자열
   * @throws MemberDomainException 비밀번호가 유효하지 않을 때
   */
  private static void validate(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new MemberDomainException("비밀번호는 빈 값이 될 수 없습니다.");
    }
  }

  /**
   * 원시 비밀번호의 유효성 검사 (생성/변경 전) - 인코딩되지 않은 원시 비밀번호에 대한 규칙 검사
   *
   * @param rawPassword 원시 비밀번호
   * @throws MemberDomainException 원시 비밀번호가 규칙에 맞지 않을 때
   */
  public static void validateRawPassword(String rawPassword) {
    if (rawPassword == null || rawPassword.trim().isEmpty()) {
      throw new MemberDomainException("비밀번호는 빈 값이 될 수 없습니다.");
    }

    if (rawPassword.length() < 8) {
      throw new MemberDomainException("비밀번호는 최소 8자 이상이어야 합니다.");
    }

    // 숫자 포함 여부 검사
    if (!rawPassword.matches(".*\\d.*")) {
      throw new MemberDomainException("비밀번호는 최소 하나의 숫자를 포함해야 합니다.");
    }

    // 문자 포함 여부 검사
    if (!rawPassword.matches(".*[a-zA-Z].*")) {
      throw new MemberDomainException("비밀번호는 최소 하나의 문자를 포함해야 합니다.");
    }
  }
}
