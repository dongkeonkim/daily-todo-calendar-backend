package com.dailytodocalendar.api.auth.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {

    private String email;
    private String password;
    private String name;

}
