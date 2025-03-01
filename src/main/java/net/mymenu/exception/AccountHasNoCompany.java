package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class AccountHasNoCompany extends MyMenuException {
    public AccountHasNoCompany(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.PRECONDITION_REQUIRED;
    }
}
