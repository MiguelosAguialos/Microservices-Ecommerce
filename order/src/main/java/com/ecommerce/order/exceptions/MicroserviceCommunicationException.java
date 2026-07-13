package com.ecommerce.order.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class MicroserviceCommunicationException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String responseBody;

    public MicroserviceCommunicationException(HttpStatusCode statusCode, String message, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
}
