package com.dailytodocalendar.domain.member.model;

import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** 회원 이름 값 객체 */
@Getter
@EqualsAndHashCode
@ToString
public class MemberName {
  private final String value;

  private MemberName(String value) {
    this.value = value;
  }

  /**
   * 회원 이름 생성 팩토리 메서드
   *
   * @param value 이름 문자열
   * @return 회원 이름 값 객체
   * @throws MemberDomainException 이름이 유효하지 않을 때
   */
  public static MemberName of(String value) {
    validate(value);
    return new MemberName(value);
  }

  /**
   * 이름 유효성 검사
   *
   * @param value 이름 문자열
   * @throws MemberDomainException 이름이 유효하지 않을 때
   */
  private static void validate(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new MemberDomainException("이름은 빈 값이 될 수 없습니다.");
    }

    if (value.length() > 50) {
      throw new MemberDomainException("이름은 50자를 초과할 수 없습니다.");
    }
  }
}
