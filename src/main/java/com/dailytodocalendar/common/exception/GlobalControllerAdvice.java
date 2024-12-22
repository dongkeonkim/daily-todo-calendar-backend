package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.dailytodocalendar.common.codes.ErrorCode.DATABASE_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseDto<?> errorHandler(ApplicationException e){
        log.error("Error occurs {}", e.toString());
        return ResponseDto.error(e.getErrorCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto<?> databaseErrorHandler(IllegalArgumentException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseDto.error(DATABASE_ERROR);
    }

}
