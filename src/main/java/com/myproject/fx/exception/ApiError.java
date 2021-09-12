package com.myproject.fx.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiError {

    private Instant dateTime;
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message){
        super();
        this.dateTime = Instant.now(Clock.systemUTC());
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this(status, message);
        errors = Arrays.asList(error);
    }
}
