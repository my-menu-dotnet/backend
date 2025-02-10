package net.mymenu.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends MyMenuException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super("Bad request");
    }
}
