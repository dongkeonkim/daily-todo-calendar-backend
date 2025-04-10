package com.dailytodocalendar.application.port.in.auth;

import com.dailytodocalendar.application.port.in.auth.dto.SignUpCommand;

/** 회원가입 유스케이스 */
public interface SignUpUseCase {

  /**
   * 회원가입 처리
   *
   * @param command 회원가입 명령
   */
  void signUp(SignUpCommand command);
}
