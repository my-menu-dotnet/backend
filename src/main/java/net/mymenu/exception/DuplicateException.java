package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends MyMenuException {
    public DuplicateException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
