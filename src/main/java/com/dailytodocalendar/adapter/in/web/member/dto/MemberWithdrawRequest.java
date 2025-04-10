package com.dailytodocalendar.adapter.in.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** 회원 탈퇴 요청 DTO */
public record MemberWithdrawRequest(
    @NotBlank(message = "이메일은 필수 입력값입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") String email,
    @NotBlank(message = "비밀번호는 필수 입력값입니다.") String password) {}
