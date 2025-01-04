package com.dailytodocalendar.api.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String accessToken;

    public static LoginResponse of(String accessToken) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.accessToken = accessToken;
        return loginResponse;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
