package com.dailytodocalendar.common.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // 인증 & 인가
  INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호가 올바르지 않습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
  INVALID_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호가 유효하지 않습니다."),

  // Not Found
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "메모를 찾을 수 없습니다."),
  TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할 일을 찾을 수 없습니다."),
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

  // 중복
  USER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
  EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

  // 유효성 검사
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 입력 항목이 누락되었습니다."),
  PASSWORD_POLICY_VIOLATION(HttpStatus.BAD_REQUEST, "비밀번호는 최소 8자 이상이며, 문자와 숫자를 포함해야 합니다."),

  // 에러
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
  EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "외부 서비스 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;
}
