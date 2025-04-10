package com.dailytodocalendar.domain.memo.exception;

import com.dailytodocalendar.domain.common.exception.DomainException;

/** 메모 도메인 예외 */
public class MemoDomainException extends DomainException {

  public MemoDomainException(String message) {
    super(message);
  }

  public MemoDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
