package com.dailytodocalendar.domain.member.model;

import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** 이메일 값 객체 */
@Getter
@EqualsAndHashCode
@ToString
public class Email {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile(
          "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

  private final String value;

  private Email(String value) {
    this.value = value;
  }

  /**
   * 이메일 생성 팩토리 메서드
   *
   * @param value 이메일 문자열
   * @return 이메일 값 객체
   * @throws MemberDomainException 이메일 형식이 유효하지 않을 때
   */
  public static Email of(String value) {
    validate(value);
    return new Email(value);
  }

  /**
   * 이메일 유효성 검사
   *
   * @param value 이메일 문자열
   * @throws MemberDomainException 이메일 형식이 유효하지 않을 때
   */
  private static void validate(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new MemberDomainException("이메일은 빈 값이 될 수 없습니다.");
    }

    if (!EMAIL_PATTERN.matcher(value).matches()) {
      throw new MemberDomainException("유효하지 않은 이메일 형식입니다: " + value);
    }
  }
}
