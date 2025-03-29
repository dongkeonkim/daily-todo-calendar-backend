package com.dailytodocalendar.common.response;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

  private String code;
  private String message;
  private T result;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  public static <T> ResponseDto<T> success(SuccessCode successCode, T result) {
    return new ResponseDto<>(
        successCode.name(), successCode.getMessage(), result, LocalDateTime.now());
  }

  public static <T> ResponseDto<T> success(SuccessCode successCode) {
    return new ResponseDto<>(
        successCode.name(), successCode.getMessage(), null, LocalDateTime.now());
  }

  public static <T> ResponseDto<T> error(ErrorCode errorCode, T result) {
    return new ResponseDto<>(errorCode.name(), errorCode.getMessage(), result, LocalDateTime.now());
  }

  public static ResponseDto<Void> error(ErrorCode errorCode) {
    return new ResponseDto<>(errorCode.name(), errorCode.getMessage(), null, LocalDateTime.now());
  }

  public static <T> ResponseDto<T> of(String code, String message, T result) {
    return new ResponseDto<>(code, message, result, LocalDateTime.now());
  }
}
