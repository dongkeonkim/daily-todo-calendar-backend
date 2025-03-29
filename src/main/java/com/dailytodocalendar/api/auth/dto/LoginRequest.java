package com.dailytodocalendar.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "이메일은 필수 항목입니다.")
  @Email(message = "유효한 이메일 주소를 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  private String password;

  @Override
  public String toString() {
    return "LoginRequest{" + "email='" + email + '\'' + '}';
  }
}
