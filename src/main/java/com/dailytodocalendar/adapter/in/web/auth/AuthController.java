package com.dailytodocalendar.adapter.in.web.auth;

import com.dailytodocalendar.adapter.in.web.auth.dto.LoginRequest;
import com.dailytodocalendar.adapter.in.web.auth.dto.LoginResponse;
import com.dailytodocalendar.adapter.in.web.auth.dto.SignUpRequest;
import com.dailytodocalendar.adapter.in.web.common.WebAdapter;
import com.dailytodocalendar.application.port.in.auth.LoginUseCase;
import com.dailytodocalendar.application.port.in.auth.SignUpUseCase;
import com.dailytodocalendar.application.port.in.auth.dto.LoginResult;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** 인증 컨트롤러 - 인바운드 어댑터 */
@Slf4j
@WebAdapter
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final SignUpUseCase signUpUseCase;
  private final LoginUseCase loginUseCase;
  private final AuthDtoMapper authDtoMapper;

  /**
   * 회원가입
   *
   * @param request 회원가입 요청
   * @return 응답 객체
   */
  @PostMapping("/sign-up")
  public ResponseDto<Void> signUp(@Valid @RequestBody SignUpRequest request) {
    log.info("회원가입 요청: {}", request.email());
    signUpUseCase.signUp(authDtoMapper.toSignUpCommand(request));
    return ResponseDto.success(SuccessCode.USER_CREATED);
  }

  /**
   * 로그인
   *
   * @param request 로그인 요청
   * @return 응답 객체
   */
  @PostMapping("/login")
  public ResponseDto<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("로그인 요청: {}", request.email());
    LoginResult result = loginUseCase.login(authDtoMapper.toLoginCommand(request));
    return ResponseDto.success(SuccessCode.LOGIN_SUCCESS, authDtoMapper.toLoginResponse(result));
  }
}
