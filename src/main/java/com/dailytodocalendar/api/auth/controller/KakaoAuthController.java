package com.dailytodocalendar.api.auth.controller;

import com.dailytodocalendar.api.auth.dto.LoginResponse;
import com.dailytodocalendar.api.auth.service.KakaoAuthService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

  private final KakaoAuthService kakaoAuthService;

  @GetMapping("/callback")
  public ResponseDto<LoginResponse> kakaoCallback(@RequestParam String code) {
    return ResponseDto.success(SuccessCode.SUCCESS, kakaoAuthService.processKakaoLogin(code));
  }

  /**
   * 카카오 계정 연결 해제 및 회원 탈퇴
   *
   * @return 처리 결과
   */
  @PostMapping("/unlink")
  public ResponseDto<Void> unlinkKakao() {
    Long memberId = SecurityUtils.getCurrentUserId();
    log.info("카카오 계정 연결 해제 요청 - memberId: {}", memberId);

    kakaoAuthService.unlinkKakaoAccount(memberId);
    return ResponseDto.success(SuccessCode.USER_DELETED);
  }
}
