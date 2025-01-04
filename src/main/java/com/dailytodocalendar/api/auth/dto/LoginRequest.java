package com.dailytodocalendar.api.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
