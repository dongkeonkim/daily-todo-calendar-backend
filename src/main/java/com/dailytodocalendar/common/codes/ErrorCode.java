package com.dailytodocalendar.common.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT,"이미 존재하는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호가 유효하지 않습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
