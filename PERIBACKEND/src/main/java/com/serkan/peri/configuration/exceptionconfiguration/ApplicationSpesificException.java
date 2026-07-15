package com.serkan.peri.configuration.exceptionconfiguration;

import lombok.Getter;

@Getter
public class ApplicationSpesificException extends RuntimeException {


    private ErrorType errorType;

    public ApplicationSpesificException(ErrorType errorType){

        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }
}
