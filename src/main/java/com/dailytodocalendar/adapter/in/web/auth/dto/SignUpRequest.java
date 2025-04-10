package com.dailytodocalendar.adapter.in.web.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 회원가입 요청 DTO */
public record SignUpRequest(
    @NotBlank(message = "이메일은 필수 입력값입니다.") @Email(message = "유효한 이메일 형식이 아닙니다.") String email,
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$", message = "비밀번호는 문자와 숫자를 포함해야 합니다.")
        String password,
    @NotBlank(message = "이름은 필수 입력값입니다.") @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
        String name) {}
