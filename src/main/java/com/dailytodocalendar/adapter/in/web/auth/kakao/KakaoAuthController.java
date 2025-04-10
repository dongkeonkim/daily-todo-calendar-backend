package com.dailytodocalendar.adapter.in.web.auth.kakao;

import com.dailytodocalendar.adapter.in.web.auth.dto.LoginResponse;
import com.dailytodocalendar.adapter.in.web.common.WebAdapter;
import com.dailytodocalendar.application.port.in.auth.KakaoAuthUseCase;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@WebAdapter
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

  private final KakaoAuthUseCase kakaoAuthUseCase;

  /**
   * 카카오 인증 콜백 처리
   *
   * @param code 카카오 인증 코드
   * @return 로그인 응답
   */
  @GetMapping("/callback")
  public ResponseDto<LoginResponse> kakaoCallback(@RequestParam String code) {
    log.info("카카오 인증 콜백 요청 - 코드: {}", code);
    var authToken = kakaoAuthUseCase.processKakaoLogin(code);
    return ResponseDto.success(SuccessCode.LOGIN_SUCCESS, LoginResponse.of(authToken.getValue()));
  }
}
