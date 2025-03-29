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

    @NotBlank(message = "현재 비밀번호는 필수 항목입니다.")
    private String password;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자 및 특수 문자를 포함해야 합니다.")
    private String newPassword;

    @Override
    public String toString() {
        return "MemberUpdateDto{" +
                "name='" + name + '\'' +
                ", newPassword='[PROTECTED]'" +
                '}';
    }
}