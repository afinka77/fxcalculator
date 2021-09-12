package com.myproject.fx.exception;

import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidCurrencyException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handle500(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                "Invalid currency is passed to service: " + ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handle400(
            Exception ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof MethodArgumentNotValidException ) {
            for (FieldError error : ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            for (ObjectError error : ((MethodArgumentNotValidException) ex).getBindingResult().getGlobalErrors()) {
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            errors.add(((MethodArgumentTypeMismatchException)ex).getName() + ": " + ex.getMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

