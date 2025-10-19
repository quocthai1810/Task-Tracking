package com.example.task_tracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.task_tracker.domain.dto.ErrorReponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorReponse> handleExceptions(
        RuntimeException ex, WebRequest request
    ) {
        ErrorReponse errorReponse = new ErrorReponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorReponse, HttpStatus.BAD_REQUEST);
    }
}
