package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class ElasticServiceManagerException extends RuntimeException{

    private final ErrorType errorType;

    public ElasticServiceManagerException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }

    public ElasticServiceManagerException(ErrorType errorType){
        this.errorType = errorType;
    }
}
