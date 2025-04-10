package com.dailytodocalendar.application.port.in.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/** 회원 탈퇴 명령 객체 - 유스케이스에 전달되는 명령 */
@Builder
public record WithdrawMemberCommand(
    @NotBlank(message = "이메일은 필수 입력값입니다.") String email,
    @NotBlank(message = "비밀번호는 필수 입력값입니다.") String password) {

  @Override
  public String toString() {
    return "WithdrawMemberCommand{" + "email='" + email + '\'' + '}';
  }
}
