package com.dailytodocalendar.api.auth.controller;

import com.dailytodocalendar.api.auth.dto.LoginRequest;
import com.dailytodocalendar.api.auth.dto.LoginResponse;
import com.dailytodocalendar.api.auth.dto.SignUpRequest;
import com.dailytodocalendar.api.auth.service.AuthService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseDto<Void> signUp(@Valid @RequestBody SignUpRequest SignUpRequest) {
        authService.signUp(SignUpRequest);
        return ResponseDto.success(SuccessCode.CREATED);
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseDto.success(SuccessCode.SUCCESS, authService.login(loginRequest));
    }

}
