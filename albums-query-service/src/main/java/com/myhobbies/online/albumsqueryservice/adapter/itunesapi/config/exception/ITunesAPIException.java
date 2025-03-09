package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.exception;

public class ITunesAPIException extends RuntimeException {
    public ITunesAPIException(String message) {
        super(message);
    }

    public ITunesAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
