package com.dailytodocalendar.domain.auth.model;

import lombok.Builder;
import lombok.Getter;

/** 인증 토큰 모델 */
@Getter
@Builder
public class AuthToken {
  private final String value;
  private final long expiration;

  public static AuthToken of(String value, long expiration) {
    return AuthToken.builder().value(value).expiration(expiration).build();
  }
}
