package com.websocket.publisher.stockprices.domain.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends ExternalClientException{
    public InternalServerException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
