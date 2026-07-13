package com.ecommerce.order.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MicroserviceCommunicationException.class)
    public ResponseEntity<Map<String, Object>> handleMicroserviceCommunicationException(MicroserviceCommunicationException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(Map.of(
                        "path", exception.getMessage(),
                        "message", exception.getResponseBody(),
                        "status", exception.getStatusCode().value()
                ));
    }
}
