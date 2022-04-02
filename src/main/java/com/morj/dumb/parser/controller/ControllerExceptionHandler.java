package com.morj.dumb.parser.controller;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConnectionPoolTimeoutException.class})
    protected ResponseEntity<Object> handleTimeout() {
        return ResponseEntity
                .internalServerError()
                .body("Too lazy to wait for so long");
    }
}
