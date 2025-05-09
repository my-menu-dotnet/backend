package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class EmailCodeRequestTooSoonException extends MyMenuException {
    public EmailCodeRequestTooSoonException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.TOO_MANY_REQUESTS;
    }
}
