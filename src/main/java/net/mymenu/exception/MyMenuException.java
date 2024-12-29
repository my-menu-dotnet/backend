package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public abstract class MyMenuException extends RuntimeException {
    public MyMenuException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
