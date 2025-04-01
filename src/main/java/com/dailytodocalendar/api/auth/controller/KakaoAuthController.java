package com.dailytodocalendar.api.auth.controller;

import com.dailytodocalendar.api.auth.dto.LoginResponse;
import com.dailytodocalendar.api.auth.service.KakaoAuthService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

  private final KakaoAuthService kakaoAuthService;

  @GetMapping("/callback")
  public ResponseDto<LoginResponse> kakaoCallback(@RequestParam String code) {
    return ResponseDto.success(SuccessCode.SUCCESS, kakaoAuthService.processKakaoLogin(code));
  }
}
