package com.dailytodocalendar.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {

    private String currentPassword;
    private String newPassword;

}
