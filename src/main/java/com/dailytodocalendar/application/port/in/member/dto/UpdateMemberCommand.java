package com.dailytodocalendar.application.port.in.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/** 회원 정보 수정 명령 객체 - 유스케이스에 전달되는 명령 */
@Builder
public record UpdateMemberCommand(
    @NotBlank(message = "이름은 필수 입력값입니다.") @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
        String name,
    String password,
    String newPassword) {

  /** 기본 검증 외 비즈니스 규칙 검증 */
  public void validate() {
    // 비밀번호 정책 검증 등 추가 검증 규칙 적용
    if (newPassword != null && !newPassword.isEmpty() && newPassword.length() < 8) {
      throw new IllegalArgumentException("새 비밀번호는 최소 8자 이상이어야 합니다.");
    }
  }
}
