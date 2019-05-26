package com.nisshoku.mgnt.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withErrorCode().withStatus()
                .withErrorMessage(exception.getMessage())
                .withUri(exception.getUri()).build();

        return new ResponseEntity<>(response, response.getStatus());
    }
}
