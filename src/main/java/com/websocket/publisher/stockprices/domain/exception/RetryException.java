package com.websocket.publisher.stockprices.domain.exception;

public class RetryException extends RuntimeException{
    public RetryException(String message){
        super(message);
    }
}
