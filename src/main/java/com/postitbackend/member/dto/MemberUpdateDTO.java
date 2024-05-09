package com.postitbackend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    private String currentPassword;
    private String newPassword;

}
