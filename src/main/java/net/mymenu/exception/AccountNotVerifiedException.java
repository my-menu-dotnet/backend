package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class AccountNotVerifiedException extends MyMenuException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
