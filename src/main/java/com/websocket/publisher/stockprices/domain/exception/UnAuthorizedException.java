package com.websocket.publisher.stockprices.domain.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ExternalClientException{
    public UnAuthorizedException(String message){
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
