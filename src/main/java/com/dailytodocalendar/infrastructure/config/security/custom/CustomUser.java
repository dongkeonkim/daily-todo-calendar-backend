package com.dailytodocalendar.infrastructure.config.security.custom;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/** 사용자 정의 User 클래스 - 스프링 시큐리티 User 확장 */
@Getter
public class CustomUser extends User {

  private final Long memberId;
  private final String email;

  public CustomUser(
      Long memberId,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(email, password, authorities);
    this.memberId = memberId;
    this.email = email;
  }
}
