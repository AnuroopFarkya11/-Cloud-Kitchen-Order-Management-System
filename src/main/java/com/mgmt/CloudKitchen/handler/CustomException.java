package com.mgmt.CloudKitchen.handler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Slf4j
@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final int numericStatus;


    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.numericStatus = status != null ? status.value() : 0;
    }

    public CustomException(String message, int numericStatus) {
        super(message);
        this.status = null;
        this.numericStatus = numericStatus;
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "message", getMessage(),
                "status", status != null ? status.toString() : numericStatus,
                "timestamp", new Date()
        );
    }
}

