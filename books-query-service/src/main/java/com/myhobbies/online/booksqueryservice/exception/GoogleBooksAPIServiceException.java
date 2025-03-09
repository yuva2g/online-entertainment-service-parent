package com.myhobbies.online.booksqueryservice.exception;

public class GoogleBooksAPIServiceException extends RuntimeException {
    public GoogleBooksAPIServiceException(String message) {
        super(message);
    }

    public GoogleBooksAPIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
