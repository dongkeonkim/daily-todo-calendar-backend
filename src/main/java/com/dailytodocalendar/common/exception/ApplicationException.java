package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.codes.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException{
    private ErrorCode errorCode;
}
