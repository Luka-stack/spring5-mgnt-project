package com.nisshoku.mgnt.exceptions;

import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.State;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders httpHeaders,
                                                                  final HttpStatus status, final WebRequest request)
    {
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        //return handleExceptionInternal(ex, response, httpHeaders, response.getStatus(), request);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
                                                         final HttpHeaders httpHeaders,
                                                         final HttpStatus status, final WebRequest request)
    {
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        //return handleExceptionInternal(ex, response, httpHeaders, response.getStatus(), request);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders httpHeaders,
                                                        final HttpStatus status, final WebRequest request)
    {
        final String error = ex.getValue() + " value of " + ex.getPropertyName()
                             + " should be of type " + ex.getRequiredType();

        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                     final HttpHeaders httpHeaders,
                                                                     final HttpStatus status, final WebRequest request)
    {
        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                          final HttpHeaders httpHeaders,
                                                                          final HttpStatus status, final WebRequest request)
    {
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                   final WebRequest request)
    {
        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            final WebRequest request)
    {
        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError response = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        final ApiError response = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(),
                ex.getMessage());

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    // 404
    @ExceptionHandler(value = {ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(final RuntimeException ex,
                                                             final WebRequest webRequest)
    {
        final ApiError response = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "Resource Not Found!");

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                         final HttpHeaders headers,
                                                                         final HttpStatus status,
                                                                         final WebRequest request)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported method are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(method -> builder.append(method).append(" "));

        final ApiError response = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    // 409
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleConflict(final RuntimeException ex, final ServletWebRequest request) {

        final StringBuilder builder = new StringBuilder();
        if (request.getRequest().getRequestURI().contains("lang")) {
            builder.append("Available languages are: ");
            builder.append(Arrays.toString(Language.values()));
        }
        else if (request.getRequest().getRequestURI().contains("state")) {
            builder.append("Available states are: ");
            builder.append(Arrays.toString(State.values()));
        }
        else {
            builder.append("You parameter are wrong. You should change it.");
        }

        final ApiError response = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), builder.toString());

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    // 415
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {

        final ApiError response = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
                                         "Unexpected error occured");
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

}