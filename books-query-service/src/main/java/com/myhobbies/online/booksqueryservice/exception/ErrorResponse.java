package com.myhobbies.online.booksqueryservice.exception;

public record ErrorResponse (int errorCode, String errorMessage) {
}