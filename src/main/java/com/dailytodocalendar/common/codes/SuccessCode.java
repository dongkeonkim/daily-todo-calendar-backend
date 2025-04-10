package com.dailytodocalendar.common.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/** 애플리케이션 성공 코드 정의 */
@Getter
@RequiredArgsConstructor
public enum SuccessCode {
  // General Success
  SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),

  // Resource Creation
  CREATED(HttpStatus.CREATED, "리소스가 생성되었습니다."),
  MEMO_CREATED(HttpStatus.CREATED, "메모가 생성되었습니다."),
  USER_CREATED(HttpStatus.CREATED, "사용자가 등록되었습니다."),

  // Resource Updates
  UPDATED(HttpStatus.OK, "리소스가 업데이트되었습니다."),
  MEMO_UPDATED(HttpStatus.OK, "메모가 업데이트되었습니다."),
  USER_UPDATED(HttpStatus.OK, "사용자 정보가 업데이트되었습니다."),
  PASSWORD_UPDATED(HttpStatus.OK, "비밀번호가 변경되었습니다."),

  // Resource Deletion
  DELETED(HttpStatus.OK, "리소스가 삭제되었습니다."),
  MEMO_DELETED(HttpStatus.OK, "메모가 삭제되었습니다."),
  USER_DELETED(HttpStatus.OK, "사용자가 삭제되었습니다."),

  // Authentication
  LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
  LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다.");

  private final HttpStatus status;
  private final String message;
}
