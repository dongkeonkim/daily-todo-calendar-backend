package com.dailytodocalendar.adapter.in.web.auth.dto;

/** 로그인 응답 DTO */
public record LoginResponse(String accessToken) {
  public static LoginResponse of(String token) {
    return new LoginResponse(token);
  }
}
