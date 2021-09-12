package com.myproject.fx.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class})
    public ResponseEntity<Object> handle400(
            Exception ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        switch (ex.getClass().getSimpleName()) {
            case "MethodArgumentNotValidException":
                errors.addAll(getErrorsFrom((MethodArgumentNotValidException) ex));
                break;
            case "MethodArgumentTypeMismatchException":
                errors.addAll(getErrorsFrom((MethodArgumentTypeMismatchException) ex));
                break;
            case "MissingServletRequestParameterException":
                errors.addAll(getErrorsFrom((MissingServletRequestParameterException) ex));
                break;
            case "ConstraintViolationException":
                errors.addAll(getErrorsFrom((ConstraintViolationException) ex));
                break;
            default:
                errors.add(ex.getLocalizedMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private List<String> getErrorsFrom(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return errors;
    }

    private List<String> getErrorsFrom(MethodArgumentTypeMismatchException ex) {
        List<String> errors = new ArrayList<>();
        if (BigDecimal.class.equals(ex.getRequiredType())) {
            errors.add("Parameter '" + ex.getName() + "' has invalid value '" + ex.getValue() + "'. Value should be positive decimal.");
        } else {
            errors.add("Parameter '" + ex.getName() + "' has invalid value '" + ex.getValue() + "'. Available values are: 'EUR','USD','GBP','BTC','ETH','FKE'");
        }
        return errors;
    }

    private List<String> getErrorsFrom(MissingServletRequestParameterException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Required request parameter '" + ex.getParameterName() + "' for method parameter type " + ex.getParameterType() + " is not present");
        return errors;
    }

    private List<String> getErrorsFrom(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            errors.add("Request parameter '" + cv.getPropertyPath() + "' has invalid value '" + cv.getInvalidValue() + "', " + cv.getMessage());
        }
        return errors;
    }
}

