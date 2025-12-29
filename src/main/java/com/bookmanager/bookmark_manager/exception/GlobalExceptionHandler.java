package com.bookmanager.bookmark_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookmarkNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(BookmarkNotFoundException ex) {

        ApiError error = new ApiError(
                ex.getMessage(),
                "BookMark_Not_Found",
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        List<ApiFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ApiFieldError(
                        e.getField(),
                        e.getDefaultMessage(),
                        "invalid_value"
                ))
                .toList();

        ApiError error = new ApiError(
                "Validation Failed",
                "Validation Error",
                errors
        );

        return ResponseEntity.badRequest().body(error);
    }
}
