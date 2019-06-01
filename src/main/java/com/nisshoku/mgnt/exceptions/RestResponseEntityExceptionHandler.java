package com.nisshoku.mgnt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(final ResourceNotFoundException exception,
                                                          WebRequest webRequest)
    {
        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withErrorMessage(exception.getMessage())
                .withUri(exception.getUri()).build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, ServletWebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(HttpStatus.CONFLICT)
                .withErrorMessage(ex.getMessage())
                .withUri(request.getRequest().getRequestURI())
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }
}