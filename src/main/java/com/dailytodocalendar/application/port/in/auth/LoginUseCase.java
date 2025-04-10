package com.dailytodocalendar.application.port.in.auth;

import com.dailytodocalendar.application.port.in.auth.dto.LoginCommand;
import com.dailytodocalendar.application.port.in.auth.dto.LoginResult;

/** 로그인 유스케이스 */
public interface LoginUseCase {

  /**
   * 로그인 처리
   *
   * @param command 로그인 명령
   * @return 로그인 결과
   */
  LoginResult login(LoginCommand command);
}
