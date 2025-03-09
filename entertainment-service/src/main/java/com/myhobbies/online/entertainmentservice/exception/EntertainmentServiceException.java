package com.myhobbies.online.entertainmentservice.exception;

public class EntertainmentServiceException extends RuntimeException {
    public EntertainmentServiceException(String message) {
        super(message);
    }

    public EntertainmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
