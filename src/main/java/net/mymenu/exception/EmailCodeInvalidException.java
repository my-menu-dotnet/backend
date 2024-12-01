package net.mymenu.exception;

public class EmailCodeInvalidException extends RuntimeException {
    public EmailCodeInvalidException() {
        super("Invalid email code");
    }
}
