package com.dailytodocalendar.common.response;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 응답 DTO
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

  private String code;
  private String message;
  private T result;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  /**
   * 성공 응답 생성 (결과 데이터 포함)
   *
   * @param successCode 성공 코드
   * @param result 결과 데이터
   * @return 응답 DTO
   */
  public static <T> ResponseDto<T> success(SuccessCode successCode, T result) {
    return new ResponseDto<>(
        successCode.name(), successCode.getMessage(), result, LocalDateTime.now());
  }

  /**
   * 성공 응답 생성 (결과 데이터 없음)
   *
   * @param successCode 성공 코드
   * @return 응답 DTO
   */
  public static <T> ResponseDto<T> success(SuccessCode successCode) {
    return new ResponseDto<>(
        successCode.name(), successCode.getMessage(), null, LocalDateTime.now());
  }

  /**
   * 에러 응답 생성 (결과 데이터 포함)
   *
   * @param errorCode 에러 코드
   * @param result 결과 데이터
   * @return 응답 DTO
   */
  public static <T> ResponseDto<T> error(ErrorCode errorCode, T result) {
    return new ResponseDto<>(errorCode.name(), errorCode.getMessage(), result, LocalDateTime.now());
  }

  /**
   * 에러 응답 생성 (결과 데이터 없음)
   *
   * @param errorCode 에러 코드
   * @return 응답 DTO
   */
  public static ResponseDto<Void> error(ErrorCode errorCode) {
    return new ResponseDto<>(errorCode.name(), errorCode.getMessage(), null, LocalDateTime.now());
  }

  /**
   * 커스텀 응답 생성
   *
   * @param code 코드
   * @param message 메시지
   * @param result 결과 데이터
   * @return 응답 DTO
   */
  public static <T> ResponseDto<T> of(String code, String message, T result) {
    return new ResponseDto<>(code, message, result, LocalDateTime.now());
  }
}
