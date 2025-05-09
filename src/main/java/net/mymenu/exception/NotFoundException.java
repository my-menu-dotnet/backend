package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends MyMenuException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
