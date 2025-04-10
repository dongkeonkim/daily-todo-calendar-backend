package com.dailytodocalendar.domain.member.exception;

import com.dailytodocalendar.domain.common.exception.DomainException;

/** 회원 도메인 관련 예외 */
public class MemberDomainException extends DomainException {

  public MemberDomainException(String message) {
    super(message);
  }

  public MemberDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
