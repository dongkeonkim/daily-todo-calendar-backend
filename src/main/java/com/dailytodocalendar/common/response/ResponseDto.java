package com.dailytodocalendar.common.response;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.codes.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private String code;
    private String message;
    private T result;
    private LocalDateTime localDateTime;

    public static <T> ResponseDto<T> success(SuccessCode successCode, T result) {
        return new ResponseDto<T>(successCode.name(), successCode.getMessage(), result, LocalDateTime.now());
    }

    public static <T> ResponseDto<T> success(SuccessCode successCode) {
        return new ResponseDto<T>(successCode.name(), successCode.getMessage(), null, LocalDateTime.now());
    }

    public static ResponseDto<Void> error(ErrorCode errorCode) {
        return new ResponseDto<>(errorCode.name(), errorCode.getMessage(), null, LocalDateTime.now());
    }

}
