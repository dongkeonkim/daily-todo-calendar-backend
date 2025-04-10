package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.domain.common.exception.DomainException;
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

/** 글로벌 예외 처리 어드바이스 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

  /** 애플리케이션 예외 처리 */
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<?> handleApplicationException(ApplicationException e) {
    log.error("애플리케이션 예외 발생: {}", e.toString());
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(ResponseDto.error(e.getErrorCode()));
  }

  /** 도메인 예외 처리 */
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<?> handleDomainException(DomainException e) {
    log.error("도메인 예외 발생: {}", e.toString());
    // 도메인 예외를 애플리케이션 예외로 변환
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR, e.getMessage()));
  }

  /** 잘못된 인자 예외 처리 */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("잘못된 인자 예외 발생: {}", e.toString());
    return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus())
        .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR, e.getMessage()));
  }

  /** 유효성 검증 실패 예외 처리 */
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

  /** 제약 조건 위반 예외 처리 */
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

  /** 데이터 조회 결과 없음 예외 처리 */
  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
    log.error("데이터를 찾을 수 없음: {}", e.toString());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ResponseDto.error(ErrorCode.RESOURCE_NOT_FOUND));
  }

  /** 데이터 무결성 위반 예외 처리 */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    log.error("데이터 무결성 위반: {}", e.toString());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ResponseDto.error(ErrorCode.DATABASE_ERROR));
  }

  /** 요청 메시지 파싱 오류 처리 */
  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<?> handleMessageNotReadableException(Exception e) {
    log.error("메시지 파싱 오류: {}", e.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR));
  }

  /** 필수 파라미터 누락 예외 처리 */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException e) {
    log.error("필수 파라미터 누락: {}", e.toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDto.error(ErrorCode.MISSING_REQUIRED_FIELD));
  }

  /** 인증 오류 예외 처리 */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
    log.error("인증 오류: {}", e.toString());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ResponseDto.error(ErrorCode.INVALID_TOKEN));
  }

  /** 접근 거부 예외 처리 */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
    log.error("접근 거부: {}", e.toString());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ResponseDto.error(ErrorCode.ACCESS_DENIED));
  }

  /** 기타 예상치 못한 예외 처리 */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUnexpectedException(Exception e) {
    log.error("예상치 못한 오류 발생: ", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseDto.error(ErrorCode.SERVER_ERROR));
  }
}
