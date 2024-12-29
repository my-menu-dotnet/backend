package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends MyMenuException {
    public TokenExpiredException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
