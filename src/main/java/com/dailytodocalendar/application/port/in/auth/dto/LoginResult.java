package com.dailytodocalendar.application.port.in.auth.dto;

import lombok.Builder;

/** 로그인 결과 객체 */
@Builder
public record LoginResult(String token) {
  public static LoginResult of(String token) {
    return LoginResult.builder().token(token).build();
  }
}
