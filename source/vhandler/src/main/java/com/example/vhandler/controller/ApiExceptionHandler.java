package com.example.vhandler.controller;

import com.example.vhandler.common.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseBody<String>> handleAllException(Exception e) {
        return ResponseEntity.of(Optional.of(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getLocalizedMessage(), null)));
    }
}
