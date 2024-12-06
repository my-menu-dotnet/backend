package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public abstract class Exception extends RuntimeException {
    public Exception(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
