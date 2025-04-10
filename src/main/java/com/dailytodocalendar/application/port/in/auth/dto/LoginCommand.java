package com.dailytodocalendar.application.port.in.auth.dto;

import lombok.Builder;

/** 로그인 명령 객체 */
@Builder
public record LoginCommand(String email, String password) {
  /** 명령 유효성 검증 */
  public void validate() {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
    }

    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
    }
  }

  @Override
  public String toString() {
    return "LoginCommand{" + "email='" + email + '\'' + '}';
  }
}
