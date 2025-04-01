package com.dailytodocalendar.api.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfo {
  private Long id;
  private String nickname;
  private String email;
}
