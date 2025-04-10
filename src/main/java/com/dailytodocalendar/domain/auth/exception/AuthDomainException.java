package com.dailytodocalendar.domain.auth.exception;

import com.dailytodocalendar.domain.common.exception.DomainException;

/** 인증 도메인 예외 */
public class AuthDomainException extends DomainException {

  public AuthDomainException(String message) {
    super(message);
  }

  public AuthDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
