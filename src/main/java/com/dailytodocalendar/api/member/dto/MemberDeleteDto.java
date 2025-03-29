package com.dailytodocalendar.api.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDeleteDto {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Override
    public String toString() {
        return "MemberDeleteDto{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}