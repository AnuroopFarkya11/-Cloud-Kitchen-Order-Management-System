package com.mgmt.CloudKitchen.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException customException) {
        HttpStatus status = customException.getStatus() != null ? customException.getStatus() : HttpStatus.valueOf(customException.getNumericStatus());

        return ResponseEntity.status(status).body(customException.toMap());
    }


}
