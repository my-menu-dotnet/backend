package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class EmailCodeExpiredException extends Exception {
    public EmailCodeExpiredException() {
        super("Email code expired");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.GONE;
    }
}
