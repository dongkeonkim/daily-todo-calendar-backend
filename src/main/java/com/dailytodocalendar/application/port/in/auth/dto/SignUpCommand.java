package com.dailytodocalendar.application.port.in.auth.dto;

import lombok.Builder;

/** 회원가입 명령 객체 */
@Builder
public record SignUpCommand(String email, String password, String name) {

  /** 명령 유효성 검증 */
  public void validate() {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
    }

    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
    }

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("이름은 필수 입력값입니다.");
    }
  }

  @Override
  public String toString() {
    return "SignUpCommand{" + "email='" + email + '\'' + ", name='" + name + '\'' + '}';
  }
}
