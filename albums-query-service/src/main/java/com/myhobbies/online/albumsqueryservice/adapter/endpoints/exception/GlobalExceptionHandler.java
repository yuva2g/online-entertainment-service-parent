package com.myhobbies.online.albumsqueryservice.adapter.endpoints.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String INVALID_REQUEST = "Invalid request";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException exception,
            WebRequest request
    ) {
        return handleExceptionInternal(exception,
                buildErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_REQUEST + ": " + exception.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return handleExceptionInternal(exception,
                buildErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_REQUEST),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return handleExceptionInternal(exception,
                buildErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_REQUEST + ": " + exception.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherException(Exception exception, WebRequest webRequest) {
        return handleExceptionInternal(exception, buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        INTERNAL_SERVER_ERROR), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    private ErrorResponse buildErrorResponse(int errorCode, String errorMessage) {
        return new ErrorResponse(errorCode, errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        
        log.error("Exception occurred in Album Query service : {} ", exception.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("Exception details in Album Query service : ", exception);
        }
        return Optional.ofNullable(body)
                .map(responseBody -> super.handleExceptionInternal(exception, responseBody, headers, status, request))
                .orElseGet(() -> super.handleExceptionInternal(exception,
                        buildErrorResponse(status.value(), exception.getLocalizedMessage()), headers, status,
                        request));
    }

}
