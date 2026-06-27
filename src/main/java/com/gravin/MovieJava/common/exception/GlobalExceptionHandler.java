package com.gravin.MovieJava.common.exception;

import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach( error ->
                validationErrorMap.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                ApiErrorResponse.of(
                        ErrorCode.VALIDATION_FAILED.getCode(),
                        ErrorCode.VALIDATION_FAILED.getMessage(),
                        request.getRequestURI(),
                        validationErrorMap
                )
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse<List<String>>> handleMethodValidationException(
            HandlerMethodValidationException exception,
            HttpServletRequest request
    ) {
        List<String> validationErrorMap = new ArrayList<>();
        exception.getAllErrors().forEach( error -> {
           validationErrorMap.add(error.getDefaultMessage());
        } );

        return ResponseEntity.internalServerError().body(
                ApiErrorResponse.of(
                        ErrorCode.VALIDATION_FAILED.getCode(),
                        ErrorCode.VALIDATION_FAILED.getMessage(),
                        request.getRequestURI(),
                        validationErrorMap
                )
        );
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse<Map<String, String>>> handleAppException(
            AppException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.internalServerError().body(
                ApiErrorResponse.of(
                        exception.getErrorCode().getCode(),
                        exception.getErrorCode().getMessage(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse<Map<String, String>>> handleRuntimeException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.internalServerError().body(
                ApiErrorResponse.of(
                        500,
                        exception.getMessage(),
                        request.getRequestURI()
                )
        );
    }
}
