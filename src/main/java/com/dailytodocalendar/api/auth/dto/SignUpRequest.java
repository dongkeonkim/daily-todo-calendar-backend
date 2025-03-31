package com.dailytodocalendar.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "이메일은 필수 항목입니다.")
  @Email(message = "유효한 이메일 주소를 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
      message = "비밀번호는 최소 하나의 문자, 숫자, 특수문자를 포함해야 합니다.")
  private String password;

  @NotBlank(message = "이름은 필수 항목입니다.")
  @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
  private String name;

  @Override
  public String toString() {
    return "SignUpRequest{" + "email='" + email + '\'' + ", name='" + name + '\'' + '}';
  }
}
