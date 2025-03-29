package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.response.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<?> handleApplicationException(ApplicationException e) {
    log.error("애플리케이션 예외 발생: {}", e.toString());
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(ResponseDto.error(e.getErrorCode()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("잘못된 인자 예외 발생: {}", e.toString());
    return ResponseEntity.status(ErrorCode.DATABASE_ERROR.getStatus())
        .body(ResponseDto.error(ErrorCode.DATABASE_ERROR));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDto<Map<String, String>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    log.warn("유효성 검증 실패: {}", errors);
    ResponseDto<Map<String, String>> response =
        ResponseDto.error(ErrorCode.VALIDATION_ERROR, errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseDto<Map<String, String>>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String propertyPath = violation.getPropertyPath().toString();
              String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
              errors.put(fieldName, violation.getMessage());
            });

    log.warn("제약 조건 위반 발생: {}", errors);
    ResponseDto<Map<String, String>> response =
        ResponseDto.error(ErrorCode.VALIDATION_ERROR, errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
    log.error("데이터를 찾을 수 없음: {}", e.toString());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ResponseDto.error(ErrorCode.USER_NOT_FOUND));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    log.error("데이터 무결성 위반: {}", e.toString());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ResponseDto.error(ErrorCode.DATABASE_ERROR));
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<?> handleMessageNotReadableException(Exception e) {
    log.error("메시지 파싱 오류: {}", e.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException e) {
    log.error("필수 파라미터 누락: {}", e.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
    log.error("인증 오류: {}", e.toString());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ResponseDto.error(ErrorCode.INVALID_TOKEN));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
    log.error("접근 거부: {}", e.toString());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ResponseDto.error(ErrorCode.INVALID_TOKEN));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUnexpectedException(Exception e) {
    log.error("예상치 못한 오류 발생: ", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseDto.error(ErrorCode.DATABASE_ERROR));
  }
}
