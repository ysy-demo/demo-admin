package com.ysy.demo.admin.core;

import com.ysy.demo.admin.core.model.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultEntity violation(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException message={}", ex.getMessage());
        return ResultEntity.invalid(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultEntity violation(MethodArgumentNotValidException ex) {
        String message = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message += fieldError.getField() + " " + fieldError.getDefaultMessage() + ", ";
        }
        log.warn("MethodArgumentNotValidException message={}", message);
        return ResultEntity.invalid(message);
    }

    @ExceptionHandler(BindException.class)
    public ResultEntity violation(BindException ex) {
        String message = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message += fieldError.getField() + " " + fieldError.getDefaultMessage() + ", ";
        }
        log.warn("BindException message={}", message);
        return ResultEntity.invalid(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultEntity violation(HttpMessageNotReadableException ex) {
        log.warn("HttpMessageNotReadableException message={}", ex.getMessage());
        return ResultEntity.FAILED_INVALID_REQUEST;
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleException(AuthorizationException e) {
        log.warn("AuthorizationException message={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new ResultEntity(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResultEntity violation(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException exception message={}", ex.getMessage());
        return ResultEntity.invalid(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultEntity handleException(Exception e) {
        log.error("handleException", e);
        return ResultEntity.FAILED_SYSTEM_ERROR;
    }

}
