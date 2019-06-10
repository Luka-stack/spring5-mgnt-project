package com.nisshoku.mgnt.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private String uri;

    public String getUri() {
        return uri;
    }

    public ResourceNotFoundException() { super(); }

    public ResourceNotFoundException(String message, String uri) { super(message); this.uri = uri; }

    public ResourceNotFoundException(String message) { super(message); }

    public ResourceNotFoundException(Throwable throwable) { super(throwable); }

    public ResourceNotFoundException(String message, Throwable throwable) { super(message, throwable); }

    public ResourceNotFoundException(String message, Throwable throwable,
                                     boolean enableSuppresion, boolean writableStackTrace)
    {
        super(message, throwable, enableSuppresion, writableStackTrace);
    }
}
