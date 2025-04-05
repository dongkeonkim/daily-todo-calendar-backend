package com.dailytodocalendar.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {

  @NotBlank(message = "이름은 필수 항목입니다.")
  @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
  private String name;

  private String password;

  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
      message = "비밀번호는 최소 하나의 문자, 숫자, 특수문자를 포함해야 합니다.")
  private String newPassword;

  @Override
  public String toString() {
    return "MemberUpdateDto{" + "name='" + name + '\'' + ", newPassword='[PROTECTED]'" + '}';
  }
}
