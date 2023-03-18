package com.websocket.publisher.stockprices.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExternalClientException extends ResponseStatusException {
    public ExternalClientException(HttpStatus status, String message){
        super(status, message);
    }
}
