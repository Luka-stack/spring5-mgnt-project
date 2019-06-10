package com.nisshoku.mgnt.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String errorMessage;
    private List<String> errors;

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String errorMessage, final List<String> errors) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String errorMessage, final String error) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
        this.errors = Collections.singletonList(error);
    }

    public void setError(final String error) {
        errors = Collections.singletonList(error);
    }
}
