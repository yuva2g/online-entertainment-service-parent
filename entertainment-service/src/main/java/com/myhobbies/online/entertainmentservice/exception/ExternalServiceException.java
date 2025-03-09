package com.myhobbies.online.entertainmentservice.exception;

import com.myhobbies.online.entertainmentservice.config.ExternalService;
import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {

    private final ExternalService externalService;
    public ExternalServiceException(String message, ExternalService externalService, Throwable cause) {
        super(message, cause);
        this.externalService = externalService;
    }
}
