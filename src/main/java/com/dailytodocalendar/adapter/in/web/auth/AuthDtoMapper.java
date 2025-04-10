package com.dailytodocalendar.adapter.in.web.auth;

import com.dailytodocalendar.adapter.in.web.auth.dto.LoginRequest;
import com.dailytodocalendar.adapter.in.web.auth.dto.LoginResponse;
import com.dailytodocalendar.adapter.in.web.auth.dto.SignUpRequest;
import com.dailytodocalendar.application.port.in.auth.dto.LoginCommand;
import com.dailytodocalendar.application.port.in.auth.dto.LoginResult;
import com.dailytodocalendar.application.port.in.auth.dto.SignUpCommand;
import org.springframework.stereotype.Component;

/** 인증 DTO 매퍼 */
@Component
public class AuthDtoMapper {

  /**
   * 회원가입 요청을 명령으로 변환
   *
   * @param request 회원가입 요청
   * @return 회원가입 명령
   */
  public SignUpCommand toSignUpCommand(SignUpRequest request) {
    return SignUpCommand.builder()
        .email(request.email())
        .password(request.password())
        .name(request.name())
        .build();
  }

  /**
   * 로그인 요청을 명령으로 변환
   *
   * @param request 로그인 요청
   * @return 로그인 명령
   */
  public LoginCommand toLoginCommand(LoginRequest request) {
    return LoginCommand.builder().email(request.email()).password(request.password()).build();
  }

  /**
   * 로그인 결과를 응답으로 변환
   *
   * @param result 로그인 결과
   * @return 로그인 응답
   */
  public LoginResponse toLoginResponse(LoginResult result) {
    return LoginResponse.of(result.token());
  }
}
