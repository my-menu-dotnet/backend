package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class AccountAlreadyVerifiedException extends Exception {
    public AccountAlreadyVerifiedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
