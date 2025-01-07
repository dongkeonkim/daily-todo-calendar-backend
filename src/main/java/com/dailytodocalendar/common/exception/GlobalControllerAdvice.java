package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.dailytodocalendar.common.codes.ErrorCode.DATABASE_ERROR;
import static com.dailytodocalendar.common.codes.ErrorCode.VALIDATION_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> errorHandler(ApplicationException e){
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ResponseDto.error(e.getErrorCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> databaseErrorHandler(IllegalArgumentException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(DATABASE_ERROR.getStatus())
                .body(ResponseDto.error(DATABASE_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ResponseDto<Map<String, String>> response = ResponseDto.error(VALIDATION_ERROR, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
