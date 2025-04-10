package com.dailytodocalendar.adapter.in.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 회원 정보 수정 요청 DTO */
public record MemberUpdateRequest(
    @NotBlank(message = "이름은 필수 입력값입니다.") @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
        String name,
    String password,
    String newPassword) {}
