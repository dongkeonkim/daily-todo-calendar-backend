package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.codes.ErrorCode;
import lombok.Getter;

/** 애플리케이션 예외 - 애플리케이션 레이어에서 발생하는 예외 처리 */
@Getter
public class ApplicationException extends RuntimeException {

  private final ErrorCode errorCode;

  public ApplicationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ApplicationException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }
}
